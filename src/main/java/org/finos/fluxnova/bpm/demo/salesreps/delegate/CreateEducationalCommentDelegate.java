package org.finos.fluxnova.bpm.demo.salesreps.delegate;

import dev.langchain4j.model.chat.ChatModel;
import org.springframework.stereotype.Component;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;

@Component("createEducationalCommentDelegate")
public class CreateEducationalCommentDelegate implements JavaDelegate {

    private final ChatModel chatModel;

    public CreateEducationalCommentDelegate(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public void execute(DelegateExecution execution) {

        String transcript = (String) execution.getVariable("transcript");
        String ratingJson = (String) execution.getVariable("ratingJson");

        String prompt = """
                You are a sales coach.

                Based on the sales communication transcript and the final approved evaluation JSON,
                write a short educational comment for the sales representative.

                Goals:
                - Be constructive and professional
                - Reinforce what they did well
                - Explain what they should improve
                - Keep it concise but useful
                - Address the sales rep directly as "you"

                Return plain text only.

                Transcript:
                %s

                Final approved evaluation JSON:
                %s
                """.formatted(transcript, ratingJson);

        String educationalComment = chatModel.chat(prompt).trim();

        System.out.println("Educational comment: " + educationalComment);

        execution.setVariable("educationalComment", educationalComment);
    }
}