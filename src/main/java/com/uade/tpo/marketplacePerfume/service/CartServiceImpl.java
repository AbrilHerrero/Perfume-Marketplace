package com.uade.tpo.marketplacePerfume.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.uade.tpo.marketplacePerfume.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final SampleRepository sampleRepository;
    private final UserRepository userRepository;

    private User currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario autenticado no encontrado"));
    }

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

    @Override
    @Transactional(readOnly = true)
    public CartResponseDTO getCart() {
        User user = currentUser();
        return cartRepository.findByBuyer_Id(user.getId())
                .map(c -> CartMapper.toCartResponseDto(c, loadLines(c.getId())))
                .orElseGet(() -> CartMapper.toEmptyCartResponse(user));
    }

    @Override
    @Transactional
    public CartItemResponseDTO addCartItem(CartItemAddDTO request) {
        User user = currentUser();
        Cart cart = getOrCreateCart(user);
        Sample sample = sampleRepository.findById(request.getSampleId()).orElseThrow(SampleNotFoundException::new);

        CartItem saved = cartItemRepository.findByCart_IdAndSample_Id(cart.getId(), sample.getId())
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + request.getQuantity());
                    return cartItemRepository.save(existing);
                })
                .orElseGet(() -> cartItemRepository.save(CartItem.builder()
                        .cart(cart)
                        .sample(sample)
                        .quantity(request.getQuantity())
                        .addedAt(LocalDateTime.now())
                        .build()));

        touchCart(cart);
        return CartMapper.toCartItemResponseDto(
                cartItemRepository.findWithDetailsById(saved.getId()).orElse(saved));
    }

    @Override
    @Transactional
    public List<CartItemResponseDTO> addCartItems(CartBulkAddDTO request) {
        List<CartItemResponseDTO> out = new ArrayList<>();
        for (CartItemAddDTO line : request.getItems()) {
            out.add(addCartItem(line));
        }
        return out;
    }

    @Override
    @Transactional(readOnly = true)
    public CartItemResponseDTO getCartItem(Long cartItemId) {
        return CartMapper.toCartItemResponseDto(requireOwnedItem(cartItemId, currentUser()));
    }

    @Override
    @Transactional
    public void updateCartItemQuantity(Long cartItemId, int quantity) {
        if (quantity < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser al menos 1");
        }
        User user = currentUser();
        CartItem ci = requireOwnedItem(cartItemId, user);
        ci.setQuantity(quantity);
        cartItemRepository.save(ci);
        touchCart(ci.getCart());
    }

    @Override
    @Transactional
    public void removeCartItem(Long cartItemId) {
        User user = currentUser();
        CartItem ci = requireOwnedItem(cartItemId, user);
        Cart cart = ci.getCart();
        cartItemRepository.delete(ci);
        touchCart(cart);
    }

    @Override
    @Transactional
    public void clearCart() {
        User user = currentUser();
        cartRepository.findByBuyer_Id(user.getId()).ifPresent(c -> {
            cartItemRepository.deleteByCart_Id(c.getId());
            touchCart(c);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public CartStockCheckResponseDTO checkCartStock() {
        User user = currentUser();
        List<CartItem> lines = cartRepository.findByBuyer_Id(user.getId())
                .map(c -> loadLines(c.getId()))
                .orElse(List.of());
        return CartMapper.toCartStockCheckResponseDto(lines);
    }

}
