package coursera.controller;

import coursera.exceptions.TestException;
import coursera.form.AddTestForm;
import coursera.form.TestForm;
import coursera.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    //получить тест
    @PreAuthorize("hasAuthority('GET_PRIVILEGE')")
    @GetMapping("{test_id}")
    public ResponseEntity<?> getTest(@PathVariable("test_id") Long t) throws TestException {
        return testService.get(t);
    }

    //отправить тест на проверку
    @PreAuthorize("hasAuthority('SUBMIT_PRIVILEGE')")
    @PutMapping("{test_id}/submit")
    public ResponseEntity<?> submitTest(@PathVariable("test_id") Long t, @RequestBody TestForm testForm) throws TestException {
        return testService.submit(t, testForm);
    }

    //добавление нового теста в определенный курс
    @PreAuthorize("hasAuthority('ADD_PRIVILEGE')")
    @PostMapping("{course_id}")
    public ResponseEntity<?> addTest(@PathVariable("course_id") Long c, @RequestBody AddTestForm testForm) throws TestException {
        return testService.add(c, testForm);
    }

    //редактирование теста, который существует
    @PreAuthorize("hasAuthority('EDIT_PRIVILEGE')")
    @PutMapping("{test_id}/edit")
    public ResponseEntity<?> editTest(@PathVariable("test_id") Long t, @RequestBody AddTestForm testForm) throws TestException {
        return testService.edit(t, testForm);
    }

    //удаление теста, который существует
    @PreAuthorize("hasAuthority('DELETE_PRIVILEGE')")
    @DeleteMapping("{test_id}")
    public ResponseEntity<?> deleteTest(@PathVariable("test_id") Long t) throws TestException {
        return testService.delete(t);
    }
}
