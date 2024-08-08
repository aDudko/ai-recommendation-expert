package org.adudko.project.service;

import org.adudko.project.model.Answer;
import org.adudko.project.model.Question;

public interface OpenAIService {

    Answer getAnswer(Question question);

}
