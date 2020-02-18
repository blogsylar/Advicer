package ru.zhdanov.advicer.Api;

public interface AnswerCallback {
    void answer(String text);
    void exception(String text);
}
