package ru.zhdanov.advicer.Api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.zhdanov.advicer.model.AnswerModel;

public class ApiRequests {

    public static final String TAG = "happy";
    private AnswerCallback answerCallback;

    public void registerAnswerCallBacks(AnswerCallback answerCallback) {
        this.answerCallback = answerCallback;
    }


    public void getAnswer() {
        Client client = RetroClient.getApiService();
        Call<AnswerModel> call = client.getAnswer();
        call.enqueue(new Callback<AnswerModel>() {
            @Override
            public void onResponse(Call<AnswerModel> call, Response<AnswerModel> response) {
                try {
                    answerCallback.answer(response.body().getText());
                } catch (Exception e) {
                    e.printStackTrace();
                    answerCallback.exception(e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Call<AnswerModel> call, Throwable t) {
                answerCallback.exception(t.getLocalizedMessage());
            }
        });
    }

}
