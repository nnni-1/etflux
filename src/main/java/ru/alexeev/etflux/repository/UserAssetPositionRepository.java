package ru.alexeev.etflux.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.alexeev.etflux.entity.UserAssetPosition;
import ru.alexeev.etflux.entity.UserProfile;
import ru.alexeev.etflux.entity.assets.Asset;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAssetPositionRepository extends JpaRepository<UserAssetPosition, Long> {
    List<UserAssetPosition> findByUserProfile(UserProfile profile);

    Optional<UserAssetPosition> findByUserProfileAndAsset(UserProfile profile, Asset asset);
}
