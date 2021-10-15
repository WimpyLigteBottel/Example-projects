package nel.marco.db.mongo;

import nel.marco.db.entity.CustomerSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DbSequenceGenerator {

    @Autowired
    private MongoOperations operations;

    public String getNextSequence(final String sequenceName) {
        // get the sequence number
        final Query query = new Query(Criteria.where("id").is(sequenceName));
        // increment the sequence number by 1
        // "sequence" should match the attribute value specified in DbSequence.java class.
        final Update update = new Update().inc("sequence", 1);
        // modify in document
        final CustomerSequence counter = operations.findAndModify(query, update,
                FindAndModifyOptions.options()
                        .returnNew(true)
                        .upsert(true),
                CustomerSequence.class);

        return !Objects.isNull(counter) ? counter.getSequence()+"" : "1";
    }
}