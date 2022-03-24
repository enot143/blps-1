package coursera.service;

import coursera.config.CustomUserDetails;
import coursera.domain.*;
import coursera.dto.QuestionDTO;
import coursera.dto.TestDTO;
import coursera.dto.VariantDTO;
import coursera.exceptions.TestException;
import coursera.form.*;
import coursera.repos.*;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;


@Service
public class TestService {
    @Autowired
    TestRepo testRepo;
    @Autowired
    QuestionRepo questionRepo;
    @Autowired
    WeekRepo weekRepo;
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

    @Transactional
    public ResponseEntity<?> add(Long c, AddTestForm testForm) throws TestException{
        Week week = weekRepo.findWeekById(c);
        if (week == null){
            throw new TestException("Week is not found", HttpStatus.NOT_FOUND);
        }
        Test t = new Test();
        t.setMinimum(testForm.getMinimum());
        t.setWeek(week);
        testRepo.save(t);
        addQuestions(t, testForm);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Transactional
    void addQuestions(Test t, AddTestForm testForm) throws TestException {
        ArrayList<QuestionForm> questions = testForm.getQuestions();
        if (questions.size() == 0) throw new TestException("Test must have more than 0 questions", HttpStatus.OK);
        for (QuestionForm question : questions){
            Question q = new Question();
            q.setTest(t);
            q.setDescription(question.getDescription());
            questionRepo.save(q);
            ArrayList<VariantForm> listOfVariants = question.getListOfVariants();
            if (listOfVariants.size() == 0) throw new TestException("Each question must have more at least 1 correct answer", HttpStatus.OK);
            for (VariantForm variant : listOfVariants){
                Variant v = new Variant();
                v.setDescription(variant.getDescription());
                v.setCorrect(variant.isCorrect());
                v.setQuestion(q);
                variantRepo.save(v);
            }
        }
    }

    @Transactional
    public ResponseEntity<?> edit(Long t, AddTestForm testForm) throws TestException {
        Test test = testRepo.findById(t).orElseThrow(() -> new TestException("Test is not found", HttpStatus.NOT_FOUND));
        test.setMinimum(testForm.getMinimum());
        testRepo.save(test);
        ArrayList<Question> existingQuestions = questionRepo.findAllByTestId(t);
        for (Question q : existingQuestions){
            variantRepo.deleteAllByQuestionId(q.getId());
            questionRepo.delete(q);
        }
        addQuestions(test, testForm);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<?> delete(Long t) throws TestException {
        Test test = testRepo.findById(t).orElseThrow(() -> new TestException("Test is not found", HttpStatus.NOT_FOUND));
        testRepo.delete(test);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<?> get(Long test) throws TestException {
        Test t = testRepo.findById(test).orElseThrow(() -> new TestException("Test is not found", HttpStatus.NOT_FOUND));
        checkRegister(t);
        TestDTO testDTO = new TestDTO();
        ArrayList<QuestionDTO> questions = new ArrayList<>();
        questionRepo.findAllByTestId(t.getId()).forEach(q -> {
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
        return new ResponseEntity<>(testDTO, HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> submit(Long test, TestForm testForm) throws TestException {
        Test t = testRepo.findById(test).orElseThrow(() -> new TestException("Test is not found", HttpStatus.NOT_FOUND));
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
            throw new TestException("you missed deadline, it was " + t.getWeek().getDeadline(), HttpStatus.OK);
        }
        //проверка наличия попыток пройти тест и их уменьшение на 1
        if (userTestRepo.getAttempts(t.getId(), user.getId()) > 0){
            userTestRepo.setAttempts(t.getId(), user.getId());
        }
        else throw new TestException("you don't have attempts", HttpStatus.OK);
        //проверка, что человек внес ответы на все вопросы в тесте
        if (answers.size() != numberOfQuestions){
            throw new TestException("not all answers there", HttpStatus.OK);
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
        HashMap<String, Double> map = new HashMap<>();
        map.put("progress of the test", progressOfTest);
        JSONObject jsonObj = new JSONObject(map);
        return new ResponseEntity<>(jsonObj, HttpStatus.OK);
    }


    private void checkRegister(Test t) throws TestException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userRepo.findUserByEmail(userDetails.getUsername());
        System.out.println((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            return;
        }
        if (userCourseRepo.checkUserCourse(t.getWeek().getCourse().getId(), user.getId()) == null){
            throw new TestException("you don't have register to this course", HttpStatus.OK);
        }
    }
}