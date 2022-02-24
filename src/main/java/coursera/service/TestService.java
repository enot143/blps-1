package coursera.service;

import coursera.config.CustomUserDetails;
import coursera.domain.Test;
import coursera.domain.User;
import coursera.domain.UserTest;
import coursera.domain.UserTestKey;
import coursera.dto.QuestionDTO;
import coursera.dto.TestDTO;
import coursera.dto.VariantDTO;
import coursera.exceptions.TestException;
import coursera.form.AnswerForm;
import coursera.form.TestForm;
import coursera.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


@Service
public class TestService {
    @Autowired
    TestRepo testRepo;
    @Autowired
    QuestionRepo questionRepo;
    @Autowired
    VariantRepo variantRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    UserCourseRepo userCourseRepo;
    @Autowired
    UserTestRepo userTestRepo;
    @Autowired
    CourseRepo courseRepo;
    User user;


    public TestDTO get(Long test) throws TestException {
        Test t = testRepo.findById(test).orElseThrow(() -> new TestException("Test is not found"));
        checkRegister(t);
        TestDTO testDTO = new TestDTO();
        ArrayList<QuestionDTO> questions = new ArrayList<QuestionDTO>();
        questionRepo.findAll().forEach(q -> {
            QuestionDTO dto = new QuestionDTO();
            dto.setId(q.getId());
            dto.setDescription(q.getDescription());
            ArrayList<VariantDTO> variants = new ArrayList<>();
            ArrayList<Long> ids = variantRepo.findVariantIdByQuestion(q.getId());
            for (int i = 0; i < ids.size(); i++){
                VariantDTO v = new VariantDTO();
                v.setId(ids.get(i));
                v.setDescription(variantRepo.findVariantDescriptionById(ids.get(i)));
                variants.add(v);
            }
            dto.setListOfAnswers(variants);
            questions.add(dto);
        });
        testDTO.setTest(t);
        testDTO.setListOfQuestions(questions);
        return testDTO;
    }


    @Transactional
    public String submit(Long test, TestForm testForm) throws TestException {
        Test t = testRepo.findById(test).orElseThrow(() -> new TestException("Test is not found"));
        int numberOfRightAnswers = 0;
        Long numberOfQuestions = testRepo.findNumberOfQuestionsInTest(t.getId());
        Date dateNow = new java.util.Date();
        ArrayList<AnswerForm> answers = testForm.getAnswers();
        checkRegister(t);
        //если не существует еще записи о прохождении теста, создать ее
        if (userTestRepo.getUserTestByUser(user) == null){
            UserTest ut = new UserTest();
            UserTestKey userTestKey = new UserTestKey();
            ut.setId(userTestKey);
            ut.setTest(t);
            ut.setStatus(false);
            ut.setUser(user);
            ut.setAttempts(5);
            ut.setProgress(0);
            userTestRepo.save(ut);
        }
        //проверка соответствия дедлайну
        if (t.getWeek().getDeadline().before(dateNow)){
            throw new TestException("you missed deadline, it was " + t.getWeek().getDeadline());
//            return new ResponseEntity<>("you missed deadline, it was " + t.getWeek().getDeadline() , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //проверка наличия попыток пройти тест и их уменьшение на 1
        if (userTestRepo.getAttempts(t.getId(), user.getId()) > 0){
            userTestRepo.setAttempts(t.getId(), user.getId());
        }
//        else return new ResponseEntity<>("you don't have attempts", HttpStatus.FORBIDDEN);
        else throw new TestException("you don't have attempts");
        //проверка, что человек внес ответы на все вопросы в тесте
        if (answers.size() != numberOfQuestions){
//            return new ResponseEntity<>("not all answers there" , HttpStatus.INTERNAL_SERVER_ERROR);
            throw new TestException("not all answers there");
        }
        //подсчет и внесение в БД проргресса теста и прогресса курса
        for (int i = 0; i < answers.size(); i++){
            ArrayList<Long> rightAnswers = variantRepo.findAnswersByQuestion(answers.get(i).getQuestion_id());
            ArrayList<Long> userAnswers = answers.get(i).getVariant_id();
            Collections.sort(rightAnswers);
            Collections.sort(userAnswers);
            if (rightAnswers.equals(userAnswers)){
                numberOfRightAnswers++;
            }
        }
        double progressOfTest = (100 * numberOfRightAnswers / (double) numberOfQuestions);
        double progressOfCourse = 0;
        ArrayList<Long> listOfTestsId = courseRepo.findAllTestsOfCourse(t.getWeek().getCourse().getId());
        for (int i = 0; i < listOfTestsId.size(); i++){
            Long p = userTestRepo.getProgress(listOfTestsId.get(i), user.getId());
            if (p != null){
                progressOfCourse =+ p;
            }
        }
        progressOfCourse = progressOfCourse / listOfTestsId.size();
        if (progressOfTest > t.getMinimum()){
            userTestRepo.setStatus(t.getId(), user.getId());
        }
        userTestRepo.setProgress(t.getId(), user.getId(), (int) progressOfTest);
        userCourseRepo.setProgress(t.getWeek().getCourse().getId(), user.getId(), (int) progressOfCourse);
        //проверка пройденности курса (есть сертификат или нет) и если нет, установка true
        if (!userCourseRepo.checkCertificateStatus(t.getWeek().getCourse().getId(), user.getId())){
            userCourseRepo.setCertificateStatus(t.getWeek().getCourse().getId(), user.getId());
        }
        return "progress of the test = " + progressOfTest;
    }


    private void checkRegister(Test t) throws TestException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userRepo.findUserByEmail(userDetails.getUsername());
        if (userCourseRepo.checkUserCourse(t.getWeek().getCourse().getId(), user.getId()) == null){
            throw new TestException("you don't have register to this course");
//            return new ResponseEntity<>("you don't have register to this course" , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}