package me.tomalka.usosportfolio.presenter;

import me.tomalka.usosportfolio.view.MVPView;

public interface MVPPresenter<T extends MVPView> {
    void onAttach(T view);
    void onDetach();
}
