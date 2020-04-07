package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.EntrepreneurshipQuestion;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.util.StringUtil;
import lombok.Setter;
import java.time.LocalDate;

@Setter
public class EntrepreneurshipCategory implements Category {
    String type;
    LocalDate activityStartDate;
    String mainActivity;
    String name;
    String address;
    LocalDate activityEndingDate;

    public void loadData(AnswerRow answerRow){
        String question = StringUtil.cleanString(answerRow.getQuestion());
        switch (EntrepreneurshipQuestion.get(question)){
            case TYPE:
                this.type = answerRow.getAnswerAsString();
                break;
            case ACTIVITY_START_DATE:
                this.activityStartDate = answerRow.getAnswerAsDate("dd/MM/yy");
                break;
            case MAIN_ACTIVITY:
                this.mainActivity = answerRow.getAnswerAsString();
                break;
            case NAME:
                this.name = answerRow.getAnswerAsString();
                break;
            case ADDRESS:
                this.address = answerRow.getAnswerAsString();
                break;
            //check final form
            case ACTIVITY_ENDING_DATE:
                this.activityEndingDate = answerRow.getAnswerAsDate("dd/MM/yy");
                break;
        }
    };
}
