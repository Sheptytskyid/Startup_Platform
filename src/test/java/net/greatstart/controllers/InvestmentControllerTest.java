package net.greatstart.controllers;

import net.greatstart.dto.DtoInvestment;
import net.greatstart.mappers.InvestmentMapper;
import net.greatstart.model.Investment;
import net.greatstart.services.InvestmentService;
import net.greatstart.services.ProjectService;
import net.greatstart.services.UserService;
import net.greatstart.validators.InvestmentValidationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static net.greatstart.JsonConverter.convertObjectToJsonBytes;
import static net.greatstart.MapperHelper.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class InvestmentControllerTest {

    @Mock
    private InvestmentValidationService investmentValidationService;
    @Mock
    private InvestmentMapper investmentMapper;
    @Mock
    private InvestmentService investmentService;
    @Mock
    private ProjectService projectService;
    @Mock
    private UserService userService;
    @InjectMocks
    private InvestmentController investmentController;

    private Investment investment;
    private DtoInvestment dtoInvestment;
    private List<Investment> investments;
    private List<DtoInvestment> dtoInvestments;
    private MockMvc mvc;

    @Captor
    private ArgumentCaptor<DtoInvestment> captor;

    @Before
    public void setUp() throws Exception {
        mvc = standaloneSetup(investmentController).build();
        investments = getTestListOfInvestments(TEST_INVEST_1, TEST_VALUE_1, TEST_COST_1, TEST_MIN_INVEST_1);
        dtoInvestments = getTestListOfDtoInvestments(TEST_INVEST_1, TEST_VALUE_1, TEST_COST_1, TEST_MIN_INVEST_1);
        investment = getTestInvestment(TEST_INVEST_1, TEST_VALUE_1, TEST_COST_1, TEST_MIN_INVEST_1);
        dtoInvestment = getTestDtoInvestment(TEST_INVEST_1, TEST_VALUE_1, TEST_COST_1, TEST_MIN_INVEST_1);
    }

    @Test(timeout = 2000)
    public void saveValidInvestmentShouldPassValidationAndInvokeSaveServiceMethodAndReturnHttpStatusOk()
            throws Exception {
        //init
        dtoInvestment.setDateOfInvestment(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        when(investmentMapper.investmentFromDto(dtoInvestment)).thenReturn(investment);
        when(investmentValidationService.validate(dtoInvestment)).thenReturn(true);
        //use
        mvc.perform(post("/api/investment/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(dtoInvestment)))
                .andExpect(status().isOk());
        //check
        verify(investmentService, times(1)).saveInvestment(captor.capture());
        assertTrue(!captor.getValue().isPaid());
        assertTrue(!captor.getValue().isVerified());
        assertTrue((dtoInvestment.getDateOfInvestment()
                .compareTo(captor.getValue().getDateOfInvestment()) <= 1)
                && (dtoInvestment.getDateOfInvestment()
                .compareTo(captor.getValue().getDateOfInvestment()) >= -1));
        verifyNoMoreInteractions(investmentService);
    }

    @Test(timeout = 2000)
    public void saveInvalidInvestmentShouldReturnToAddInvestmentPage() throws Exception {
        /*when(principal.getName()).thenReturn(TEST_EMAIL);
        when(userService.getUserByEmail(TEST_EMAIL)).thenReturn(user);
        when(projectService.getProjectById(1)).thenReturn(project);
        when(investmentValidationService.validate(new BigDecimal(2500), project))
                .thenReturn("Some error.");
        mvc.perform(post("/project/1/investment/add")
                .principal(principal)
                .param("sum", "2500"))
                .andExpect(view().name("investment/add_investment"))
                .andExpect(model().attribute("message", "Some error."))
                .andExpect(model().attribute("project", project))
                .andExpect(model().attribute("investedSum", new BigDecimal(5000)));
        verifyNoMoreInteractions(investmentService);*/
    }

    @Test(timeout = 2000)
    public void getAllInvestmentsShouldReturnOkRequestIfAtLeastOneInvestmentExists() throws Exception {
        when(investmentService.getAllDtoInvestments()).thenReturn(dtoInvestments);
        mvc.perform(get("/api/investment"))
                .andExpect(status().isOk());
        verify(investmentService, times(1)).getAllDtoInvestments();
        verifyNoMoreInteractions(investmentService);
    }

    @Test(timeout = 2000)
    public void getAllInvestmentsShouldReturnNotFoundRequestIfThereIsNoInvestment() throws Exception {
        List<DtoInvestment> emptyList = new ArrayList<>();
        when(investmentService.getAllDtoInvestments()).thenReturn(emptyList);
        mvc.perform(get("/api/investment"))
                .andExpect(status().isNotFound());
        verify(investmentService, times(1)).getAllDtoInvestments();
        verifyNoMoreInteractions(investmentService);

    }

    @Test(timeout = 2000)
    public void getInvestmentByValidIdShouldReturnHttpStatusOk() throws Exception {
        when(investmentService.getDtoInvestmentById(1L)).thenReturn(dtoInvestment);
        mvc.perform(get("/api/investment/1"))
                .andExpect(status().isOk());
        verify(investmentService, times(1)).getDtoInvestmentById(TEST_VALUE_1);
        verifyNoMoreInteractions(investmentService);
    }

    @Test(timeout = 2000)
    public void getInvestmentByInvalidIdShouldReturnHttpStatusBadRequest() throws Exception {
        when(investmentService.getDtoInvestmentById(12L)).thenReturn(null);
        mvc.perform(get("/api/investment/12"))
                .andExpect(status().isNotFound());
        verify(investmentService, times(1)).getDtoInvestmentById(12);
        verifyNoMoreInteractions(investmentService);
    }
}