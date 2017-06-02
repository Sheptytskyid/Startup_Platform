package net.greatstart.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = "dtoInvestments")
public class DtoUserProfile {
    private Long id;
    @NotNull
    @Size(min = 2, max = 35, message = "{name.size}")
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String lastName;
    private byte[] photo;
    private List<DtoInvestment> dtoInvestments;

    @JsonSerialize
    public String initial() {
        StringBuilder initials = new StringBuilder();
        if (lastName != null && !lastName.isEmpty()) {
            initials.append(name.substring(0, 1).toUpperCase())
                    .append(".")
                    .append(lastName.substring(0, 1).toUpperCase())
                    .append(".");
        } else {
            initials.append(name.substring(0, 1).toUpperCase());
        }
        return initials.toString();
    }
}
