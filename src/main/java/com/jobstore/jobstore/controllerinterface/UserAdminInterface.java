package com.jobstore.jobstore.controllerinterface;



public interface UserAdminInterface {
    //Controller Admin 함수 정의
    default String joinAdmin(){
        return null;
    };
      void createAdmin();

    default String loginAdmin(){
        return null;
    };

    //Controller User 함수 정의
    default String joinUser(){ return null; }

    void createUser();

    default String loginUser(){ return null; }
}
