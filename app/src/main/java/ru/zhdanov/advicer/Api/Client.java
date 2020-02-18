package ru.zhdanov.advicer.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.zhdanov.advicer.model.AnswerModel;

public interface Client {
    @GET("api/random")
    Call<AnswerModel> getAnswer();
}
