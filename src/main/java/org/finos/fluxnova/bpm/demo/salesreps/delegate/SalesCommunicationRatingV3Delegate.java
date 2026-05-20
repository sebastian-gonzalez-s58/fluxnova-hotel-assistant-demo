package org.finos.fluxnova.bpm.demo.salesreps.delegate;

import dev.langchain4j.model.chat.ChatModel;
import org.springframework.stereotype.Component;
import org.finos.fluxnova.bpm.engine.delegate.DelegateExecution;
import org.finos.fluxnova.bpm.engine.delegate.JavaDelegate;

@Component("salesCommunicationRatingV3Delegate")
public class SalesCommunicationRatingV3Delegate implements JavaDelegate {

    private final ChatModel chatModel;

    public SalesCommunicationRatingV3Delegate(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public void execute(DelegateExecution execution) {

        String evaluationInput = (String) execution.getVariable("evaluationInput");
        String managerReviewReason = (String) execution.getVariable("managerReviewReason");

        String managerFeedbackSection = "";
        if (managerReviewReason != null && !managerReviewReason.isBlank()) {
            managerFeedbackSection = """
                    
                    A manager reviewed a previous version of this evaluation and disagreed.
                    The manager provided the following feedback:

                    %s

                    Update the evaluation while taking this feedback into account.
                    If the feedback is valid, adjust the grades and comments accordingly.
                    """.formatted(managerReviewReason);
        }

        String prompt = """
                You are a sales communication evaluator.

                You will receive a transcript of a sales communication.
                The communication channel will always be explicitly specified as either:
                - email
                - phone_call

                Your task is to evaluate the sales representative on exactly 5 metrics.

                Metrics and grading guidelines:

                1. clarity_of_communication
                - A: message is very clear, easy to follow, well structured, and free of major ambiguity
                - B: mostly clear, minor ambiguity, but overall understandable
                - C: somewhat confusing, disorganized, or vague in important parts
                - D: unclear, hard to follow, or poorly explained

                2. professionalism_and_tone
                - A: highly professional, respectful, confident, and appropriate tone throughout
                - B: mostly professional, minor tone issues
                - C: inconsistent professionalism or awkward tone
                - D: unprofessional, rude, careless, or inappropriate tone

                3. needs_discovery
                - A: asks strong questions, identifies customer needs clearly, and adapts communication well
                - B: shows decent understanding of customer needs but misses some opportunities
                - C: limited discovery, assumes too much, weak understanding of needs
                - D: does not explore needs or misunderstands the customer

                4. objection_handling
                - A: handles concerns clearly, confidently, and persuasively
                - B: addresses objections reasonably well with minor gaps
                - C: weak or incomplete responses to objections
                - D: avoids objections, mishandles them, or creates more confusion

                5. closing_effectiveness
                - A: closes strongly with a clear next step or commitment
                - B: decent closing effort, though not fully optimized
                - C: weak close, vague next steps, or limited attempt to move forward
                - D: no meaningful close or fails to guide next action

                Return ONLY valid JSON.
                Do not wrap it in markdown.
                Do not add explanations outside the JSON.

                The JSON must follow exactly this structure:

                {
                  "channel": "email or phone_call",
                  "metrics": {
                    "clarity_of_communication": {
                      "grade": "A|B|C|D",
                      "reason": "short reason"
                    },
                    "professionalism_and_tone": {
                      "grade": "A|B|C|D",
                      "reason": "short reason"
                    },
                    "needs_discovery": {
                      "grade": "A|B|C|D",
                      "reason": "short reason"
                    },
                    "objection_handling": {
                      "grade": "A|B|C|D",
                      "reason": "short reason"
                    },
                    "closing_effectiveness": {
                      "grade": "A|B|C|D",
                      "reason": "short reason"
                    }
                  },
                  "did_right": [
                    "thing 1",
                    "thing 2"
                  ],
                  "did_wrong": [
                    "thing 1",
                    "thing 2"
                  ]
                }

                Important rules:
                - Return exactly 2 items in did_right
                - Return exactly 2 items in did_wrong
                - Be strict and realistic in grading
                - Base your evaluation only on the provided communication
                - The channel must match the provided channel exactly
                %s

                Here is the communication to evaluate:

                %s
                """.formatted(managerFeedbackSection, evaluationInput);

        String response = chatModel.chat(prompt);

        String cleanedResponse = response
                .replace("```json", "")
                .replace("```", "")
                .trim();

        System.out.println("Sales communication rating V3 JSON: " + cleanedResponse);

        execution.setVariable("ratingJson", cleanedResponse);
    }
}