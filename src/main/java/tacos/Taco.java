package tacos;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Taco {

    private Long id;

    private Date createAd;

    @NotNull
    @Size(min = 5, message = "Name must be 5 characters long.")
    private String name;

    @NotNull(message = "You must choose at least 2 ingredients.")
    @Size(min = 2, message = "You must choose at least 1 ingredients.")
    private List<String> ingredients;
}
