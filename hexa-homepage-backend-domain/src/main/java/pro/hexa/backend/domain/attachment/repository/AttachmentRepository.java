package pro.hexa.backend.domain.attachment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.hexa.backend.domain.attachment.domain.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment,Long>, AttachmentRepositoryCustom {

}
