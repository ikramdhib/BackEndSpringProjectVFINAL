package tn.esprit.pidev.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidev.Repositories.QuestionRepository;
import tn.esprit.pidev.entities.Question;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class QuestionServiceImpl implements IServiceQuestion {
    private QuestionRepository questionRepository;
    private static final String UPLOAD_DIR = "/assets/images";
    @Override
    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public List<Question> getQuestion() {
        return questionRepository.findAll();
    }

    @Override
    public Question getQuestionById(String id) {
        return questionRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteQuestion(String id) {
        questionRepository.deleteById(id);
    }
}
