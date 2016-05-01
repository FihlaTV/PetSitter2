package com.zekisanmobile.petsitter2.asyncTask;

import android.os.AsyncTask;

import com.zekisanmobile.petsitter2.PetSitterApp;
import com.zekisanmobile.petsitter2.api.ApiService;
import com.zekisanmobile.petsitter2.api.body.SearchSittersBody;
import com.zekisanmobile.petsitter2.event.UpdateSittersEvent;
import com.zekisanmobile.petsitter2.model.SitterModel;
import com.zekisanmobile.petsitter2.view.owner.SearchResultsView;
import com.zekisanmobile.petsitter2.vo.Sitter;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Retrofit;

public class SearchSittersTask extends AsyncTask<String, Void, List<Sitter>> {

    private SearchResultsView view;
    private long owner_id;
    private List<String> animals = new ArrayList<>();
    private List<Sitter> sitters = new ArrayList<>();

    @Inject
    Retrofit retrofit;

    public SearchSittersTask(SearchResultsView view, long owner_id, ArrayList<String> animals) {
        ((PetSitterApp) view.getPetSitterApp()).getAppComponent().inject(this);
        this.view = view;
        this.owner_id = owner_id;
        this.animals = animals;
    }

    @Override
    protected List<Sitter> doInBackground(String... params) {
        SearchSittersBody body = new SearchSittersBody();
        body.setAnimals(animals);

        ApiService service = retrofit.create(ApiService.class);
        Call<List<Sitter>> call = service.searchSitters(owner_id, body);

        try {
            sitters = call.execute().body();

            for (Sitter sitter : sitters) {
                Realm realm = Realm.getDefaultInstance();

                SitterModel sitterModel = new SitterModel(realm);
                sitterModel.save(sitter);

                realm.close();
            }

            if (sitters != null) {
                return sitters;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Sitter> sitters) {
        if (sitters != null) {
            EventBus.getDefault().post(new UpdateSittersEvent(sitters));
        }
    }
}
