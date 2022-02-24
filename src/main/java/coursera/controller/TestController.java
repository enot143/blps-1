package coursera.controller;

import coursera.dto.TestDTO;
import coursera.exceptions.TestException;
import coursera.form.TestForm;
import coursera.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("{test_id}")
    public TestDTO getTest(@PathVariable("test_id") Long t) throws TestException {
        return testService.get(t);
    }

    @PutMapping("{test_id}/submit")
    public String submitTest(@PathVariable("test_id") Long t, @RequestBody TestForm testForm) throws TestException {
        return testService.submit(t, testForm);
    }
}
