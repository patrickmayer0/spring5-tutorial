package tacos.data;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tacos.Ingredient;
import tacos.Taco;

@Repository
public class JdbcTacoRepository implements TacoRepository {

    private JdbcTemplate jdbc;
    
    private IngredientRepository ingredientRepo;

    @Autowired
    public JdbcTacoRepository(IngredientRepository ingredientRepo) {
        // work around required to get ingredient repository at this point ...
        this.ingredientRepo = ingredientRepo;
    }
    
    @Override
    public Taco save(Taco taco) {
        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        
        for (String ingredientId : taco.getIngredients()) {
            // work around due to model mismatch: Taco stores String, not Ingredient instances.
            Ingredient ingredient = ingredientRepo.findOne(ingredientId);
            saveIngredientToTaco(ingredient, tacoId);
        }
        return taco;
    }

    private long saveTacoInfo(Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(
                "insert into Taco (name, createdAt) values (?, ?)", Types.VARCHAR, Types.TIMESTAMP);
        PreparedStatementCreator psc = factory.newPreparedStatementCreator(
                Arrays.asList(
                        taco.getName(), new Timestamp(taco.getCreatedAt().getTime())));
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc, keyHolder);
        
        return keyHolder.getKey().longValue();
    }

    private void saveIngredientToTaco(Ingredient ingredient, Long tacoId) {
        jdbc.update("insert into Taco_Ingredients (taco, ingredient) values (?, ?)", tacoId, ingredient.getId());
    }
}
