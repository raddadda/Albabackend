package com.jobstore.jobstore.controllerinterface;



public interface UserAdminInterface {
    //Controller Admin 함수
    default String joinAdmin(){
        return null;
    };
      void createAdmin();

    default String loginAdmin(){
        return null;
    };
}
