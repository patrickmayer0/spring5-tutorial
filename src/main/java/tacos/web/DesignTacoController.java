package tacos.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Order;
import tacos.Taco;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

    private static final String TACO = "taco";

    private static final String DESIGN = "design";

    private final IngredientRepository ingredientRepo;

    private TacoRepository designRepo;

    // create a model attribute "order" if it does not exist.
    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    // create a model attribute "taco".
    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @Autowired
    public DesignTacoController(IngredientRepository repository, TacoRepository designRepo) {
        this.ingredientRepo = repository;
        this.designRepo = designRepo;
    }

    @GetMapping
    public String showDesignForm(Model model) {
        populateDesignForm(model);
        return DESIGN;
    }

    private void populateDesignForm(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(i -> ingredients.add(i));
        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }
    }

    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors, @ModelAttribute Order order, Model model) {

        if (errors.hasErrors()) {
            populateDesignForm(model);
            return "design";
        }

        Taco saved = designRepo.save(design);
        order.addDesign(saved);

        return "redirect:/orders/current";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
    }
}
