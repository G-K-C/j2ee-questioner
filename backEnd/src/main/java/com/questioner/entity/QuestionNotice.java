package com.questioner.entity;

import javax.persistence.*;

@Entity
public class QuestionNotice {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Boolean hasRead = false;

    @Column
    private int type = -1;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "answer_id")
    private Answer answer ;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getHasRead() {
        return hasRead;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setHasRead(Boolean hasRead) {
        this.hasRead = hasRead;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "QuestionNotice{" +
                "id=" + id +
                ", hasRead=" + hasRead +
                ", question=" + question +
                ", answer=" + answer +
                ", account=" + account +
                '}';
    }
}
