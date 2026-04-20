package com.uade.tpo.marketplacePerfume.service.cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplacePerfume.entity.Cart;
import com.uade.tpo.marketplacePerfume.entity.CartItem;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.cart.CartResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemAdd;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItem.CartItemResponse;
import com.uade.tpo.marketplacePerfume.exceptions.cart.CartItemInvalidQuantityException;
import com.uade.tpo.marketplacePerfume.exceptions.cart.CartItemInsufficientStockException;
import com.uade.tpo.marketplacePerfume.exceptions.cart.CartItemNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.cart.CartNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.sample.SampleNotFoundException;
import com.uade.tpo.marketplacePerfume.mapper.CartItemMapper;
import com.uade.tpo.marketplacePerfume.mapper.CartMapper;
import com.uade.tpo.marketplacePerfume.repository.CartItemRepository;
import com.uade.tpo.marketplacePerfume.repository.CartRepository;
import com.uade.tpo.marketplacePerfume.repository.SampleRepository;
import com.uade.tpo.marketplacePerfume.repository.UserRepository;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(User user) {
        return cartRepository.findByBuyer_Id(user.getId())
                .map(CartMapper::toResponse)
                .orElseGet(CartServiceImpl::emptyCartResponse);
    }

    private static CartResponse emptyCartResponse() {
        CartResponse dto = new CartResponse();
        dto.setItems(Collections.emptyList());
        dto.setTotalPrice(BigDecimal.ZERO);
        return dto;
    }

    @Override
    @Transactional
    public CartItemResponse getCartItem(User user, Long cartItemId) {
        Cart cart = findCart(user, CartItemNotFoundException::new);
        CartItem item = findOwnedCartItem(cart, cartItemId);
        return CartItemMapper.toResponse(item);
    }

    @Override
    @Transactional
    public CartItemResponse addCartItem(User user, CartItemAdd cartItemAdd) {
        Cart cart = getOrCreateCart(user);
        CartItem saved = addOrMergeItem(cart, cartItemAdd.getSampleId(), requireOrderedQuantity(cartItemAdd.getQuantity()));
        touchCart(cart);
        return CartItemMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CartItemResponse updateCartItemQuantity(User user, Long cartItemId, Integer quantity) {
        int q = requireOrderedQuantity(quantity);
        Cart cart = findCart(user, CartItemNotFoundException::new);
        CartItem item = findOwnedCartItem(cart, cartItemId);
        Sample sample = item.getSample();
        requireActiveSample(sample);
        validateStock(sample, q);
        item.setQuantity(q);
        touchCart(cart);
        return CartItemMapper.toResponse(item);
    }

    @Override
    @Transactional
    public void removeCartItem(User user, Long cartItemId) {
        Cart cart = findCart(user, CartItemNotFoundException::new);
        CartItem item = findOwnedCartItem(cart, cartItemId);
        cartItemRepository.delete(item);
        touchCart(cart);
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        Cart cart = findCart(user, CartNotFoundException::new);
        cartItemRepository.deleteAllByCart_Id(cart.getId());
        touchCart(cart);
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByBuyer_Id(user.getId())
                .orElseGet(() -> createCartFor(user.getId()));
    }

    private Cart findCart(User user, Supplier<? extends RuntimeException> notFoundSupplier) {
        return cartRepository.findByBuyer_Id(user.getId())
                .orElseThrow(notFoundSupplier);
    }

    private Cart createCartFor(Long buyerId) {
        LocalDateTime now = LocalDateTime.now();
        Cart cart = Cart.builder()
                .buyer(userRepository.getReferenceById(buyerId))
                .createdAt(now)
                .updatedAt(now)
                .build();
        return cartRepository.save(cart);
    }

    private CartItem findOwnedCartItem(Cart cart, Long cartItemId) {
        return cartItemRepository.findByIdAndCart_Id(cartItemId, cart.getId())
                .orElseThrow(CartItemNotFoundException::new);
    }

    private CartItem addOrMergeItem(Cart cart, Long sampleId, int quantity) {
        Sample sample = sampleRepository.findById(sampleId)
                .orElseThrow(SampleNotFoundException::new);
        requireActiveSample(sample);

        return cartItemRepository.findByCart_IdAndSample_Id(cart.getId(), sampleId)
                .map(existing -> {
                    int newQuantity = existing.getQuantity() + quantity;
                    validateStock(sample, newQuantity);
                    existing.setQuantity(newQuantity);
                    return existing;
                })
                .orElseGet(() -> {
                    validateStock(sample, quantity);
                    CartItem newItem = CartItem.builder()
                            .cart(cart)
                            .sample(sample)
                            .quantity(quantity)
                            .addedAt(LocalDateTime.now())
                            .build();
                    return cartItemRepository.save(newItem);
                });
    }

    private void requireActiveSample(Sample sample) {
        if (sample == null || !sample.isActive()) {
            throw new SampleNotFoundException();
        }
    }

    private int requireOrderedQuantity(Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new CartItemInvalidQuantityException();
        }
        return quantity;
    }

    private void validateStock(Sample sample, int quantity) {
        if (quantity < 1) {
            throw new CartItemInvalidQuantityException();
        }
        if (sample == null || sample.getStock() < quantity) {
            throw new CartItemInsufficientStockException();
        }
    }

    private void touchCart(Cart cart) {
        cart.setUpdatedAt(LocalDateTime.now());
    }
}
