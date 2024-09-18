package com.bookface.comms.domain.repository;

import com.bookface.comms.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend,Long> {
    Optional<List<Friend>> findByRecipientId(Long recipientId);
}
