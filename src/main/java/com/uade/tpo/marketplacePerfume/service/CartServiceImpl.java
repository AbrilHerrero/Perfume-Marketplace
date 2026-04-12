package com.uade.tpo.marketplacePerfume.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.marketplacePerfume.entity.Cart;
import com.uade.tpo.marketplacePerfume.entity.CartItem;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs.CartBulkAddDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs.CartResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs.CartStockCheckResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs.CartItemAddDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs.CartItemResponseDTO;
import com.uade.tpo.marketplacePerfume.exceptions.CartItemNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.SampleNotFoundException;
import com.uade.tpo.marketplacePerfume.mapper.CartMapper;
import com.uade.tpo.marketplacePerfume.repository.CartItemRepository;
import com.uade.tpo.marketplacePerfume.repository.CartRepository;
import com.uade.tpo.marketplacePerfume.repository.SampleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final SampleRepository sampleRepository;

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByBuyer_Id(user.getId()).orElseGet(() -> {
            Cart c = Cart.builder()
                    .buyer(user)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            return cartRepository.save(c);
        });
    }

    private List<CartItem> loadLines(Long cartId) {
        if (cartId == null) {
            return List.of();
        }
        return cartItemRepository.findWithDetailsByCart_Id(cartId);
    }

    private CartItem requireOwnedItem(Long cartItemId, User user) {
        CartItem ci = cartItemRepository.findWithDetailsById(cartItemId).orElseThrow(CartItemNotFoundException::new);
        if (!ci.getCart().getBuyer().getId().equals(user.getId())) {
            throw new CartItemNotFoundException();
        }
        return ci;
    }

    private void touchCart(Cart cart) {
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    private void requireCartQuantityAtLeastOne(int quantity) {
        if (quantity < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be at least 1");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponseDTO getCart(User user) {
        return cartRepository.findByBuyer_Id(user.getId())
                .map(c -> CartMapper.toCartResponseDto(c, loadLines(c.getId())))
                .orElseGet(CartMapper::toEmptyCartResponse);
    }

    @Override
    @Transactional
    public CartItemResponseDTO addCartItem(User user, CartItemAddDTO request) {
        int quantity = request.getQuantity();
        requireCartQuantityAtLeastOne(quantity);

        Cart cart = getOrCreateCart(user);
        Sample sample = sampleRepository.findById(request.getSampleId()).orElseThrow(SampleNotFoundException::new);

        CartItem saved = cartItemRepository.findByCart_IdAndSample_Id(cart.getId(), sample.getId())
                .map(existing -> {
                    try {
                        existing.setQuantity(Math.addExact(existing.getQuantity(), quantity));
                    } catch (ArithmeticException e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Cart line quantity exceeds maximum");
                    }
                    return cartItemRepository.save(existing);
                })
                .orElseGet(() -> cartItemRepository.save(CartItem.builder()
                        .cart(cart)
                        .sample(sample)
                        .quantity(quantity)
                        .addedAt(LocalDateTime.now())
                        .build()));

        touchCart(cart);
        return CartMapper.toCartItemResponseDto(
                cartItemRepository.findWithDetailsById(saved.getId()).orElse(saved));
    }

    @Override
    @Transactional
    public List<CartItemResponseDTO> addCartItems(User user, CartBulkAddDTO request) {
        List<CartItemResponseDTO> out = new ArrayList<>();
        for (CartItemAddDTO line : request.getItems()) {
            out.add(addCartItem(user, line));
        }
        return out;
    }

    @Override
    @Transactional(readOnly = true)
    public CartItemResponseDTO getCartItem(User user, Long cartItemId) {
        return CartMapper.toCartItemResponseDto(requireOwnedItem(cartItemId, user));
    }

    @Override
    @Transactional
    public void updateCartItemQuantity(User user, Long cartItemId, int quantity) {
        requireCartQuantityAtLeastOne(quantity);
        CartItem ci = requireOwnedItem(cartItemId, user);
        ci.setQuantity(quantity);
        cartItemRepository.save(ci);
        touchCart(ci.getCart());
    }

    @Override
    @Transactional
    public void removeCartItem(User user, Long cartItemId) {
        CartItem ci = requireOwnedItem(cartItemId, user);
        Cart cart = ci.getCart();
        cartItemRepository.delete(ci);
        touchCart(cart);
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        cartRepository.findByBuyer_Id(user.getId()).ifPresent(c -> {
            cartItemRepository.deleteByCart_Id(c.getId());
            touchCart(c);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public CartStockCheckResponseDTO checkCartStock(User user) {
        List<CartItem> lines = cartRepository.findByBuyer_Id(user.getId())
                .map(c -> loadLines(c.getId()))
                .orElse(List.of());
        return CartMapper.toCartStockCheckResponseDto(lines);
    }

}
