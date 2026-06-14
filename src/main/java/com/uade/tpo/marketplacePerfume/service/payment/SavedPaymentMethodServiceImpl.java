package com.uade.tpo.marketplacePerfume.service.payment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplacePerfume.entity.SavedPaymentMethod;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.payment.CreateSavedPaymentMethodRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.payment.SavedPaymentMethodResponse;
import com.uade.tpo.marketplacePerfume.exceptions.payment.SavedPaymentMethodNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.user.UserNotFoundException;
import com.uade.tpo.marketplacePerfume.mapper.SavedPaymentMethodMapper;
import com.uade.tpo.marketplacePerfume.repository.SavedPaymentMethodRepository;
import com.uade.tpo.marketplacePerfume.repository.UserRepository;

@Service
public class SavedPaymentMethodServiceImpl implements ISavedPaymentMethodService {

    @Autowired
    private SavedPaymentMethodRepository savedPaymentMethodRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<SavedPaymentMethodResponse> listSavedMethods(User buyer) {
        User user = getManagedUser(buyer);
        return savedPaymentMethodRepository.findAllByBuyer_IdAndActiveTrueOrderByIdAsc(user.getId()).stream()
                .map(SavedPaymentMethodMapper::toResponse)
                .toList();
    }

    @Override
    public SavedPaymentMethodResponse createSavedMethod(CreateSavedPaymentMethodRequest request, User buyer) {
        User user = getManagedUser(buyer);
        SavedPaymentMethod entity = SavedPaymentMethodMapper.toNewEntity(request, user);
        entity.setCreatedAt(LocalDateTime.now());
        SavedPaymentMethod saved = savedPaymentMethodRepository.save(entity);
        return SavedPaymentMethodMapper.toResponse(saved);
    }

    @Override
    public void deleteSavedMethod(Long id, User buyer) {
        User user = getManagedUser(buyer);
        SavedPaymentMethod method = savedPaymentMethodRepository.findByIdAndBuyer_IdAndActiveTrue(id, user.getId())
                .orElseThrow(SavedPaymentMethodNotFoundException::new);
        method.setActive(false);
        savedPaymentMethodRepository.save(method);
    }

    private User getManagedUser(User currentUser) {
        return userRepository.findById(currentUser.getId())
                .orElseThrow(UserNotFoundException::new);
    }
}
