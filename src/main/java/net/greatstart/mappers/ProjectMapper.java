package net.greatstart.mappers;

import net.greatstart.dto.DtoInvestment;
import net.greatstart.dto.DtoProject;
import net.greatstart.model.Investment;
import net.greatstart.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mappings({
            @Mapping(source = "investments", target = "dtoInvestments"),
            @Mapping(target = "owner.dtoInvestments", ignore = true),
            @Mapping(target = "owner.photo", ignore = true)})
    DtoProject fromProjectToDto(Project project);

    Project projectFromDto(DtoProject dtoProject);

    List<DtoInvestment> investmentsToDtoInvestments(List<Investment> investments);

    @Mappings({
            @Mapping(target = "project", ignore = true),
            //@Mapping(target = "project.owner.photo", ignore = true),
            @Mapping(target = "investor", ignore = true),})
    DtoInvestment fromInvestmentToDto(Investment investment);

}
