package com.pmzhongguo.ipfs.entity;

public class IpfsProjectWithBLOBs extends IpfsProject {
    private String particular;

    private String particularE;

    private String allotDesc;

    private String allotDescE;

    private String question;

    private String questionE;

    private String riskWarning;

    private String riskWarningE;

    public String getParticular() {
        return particular;
    }

    public void setParticular(String particular) {
        this.particular = particular == null ? null : particular.trim();
    }

    public String getAllotDesc() {
        return allotDesc;
    }

    public void setAllotDesc(String allotDesc) {
        this.allotDesc = allotDesc == null ? null : allotDesc.trim();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question == null ? null : question.trim();
    }

    public String getRiskWarning() {
        return riskWarning;
    }

    public void setRiskWarning(String riskWarning) {
        this.riskWarning = riskWarning == null ? null : riskWarning.trim();
    }

    public String getParticularE() {
        return particularE;
    }

    public void setParticularE(String particularE) {
        this.particularE = particularE;
    }

    public String getAllotDescE() {
        return allotDescE;
    }

    public void setAllotDescE(String allotDescE) {
        this.allotDescE = allotDescE;
    }

    public String getQuestionE() {
        return questionE;
    }

    public void setQuestionE(String questionE) {
        this.questionE = questionE;
    }

    public String getRiskWarningE() {
        return riskWarningE;
    }

    public void setRiskWarningE(String riskWarningE) {
        this.riskWarningE = riskWarningE;
    }
}