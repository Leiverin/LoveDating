package com.project.lovedatingapp.interfaces;

import com.project.lovedatingapp.models.User;

public interface IOnClickCard {
    void onClickBtnLike(User user);
    void onClickBtnMessage(User user);
    void onClickBtnDelete(User user);
}
