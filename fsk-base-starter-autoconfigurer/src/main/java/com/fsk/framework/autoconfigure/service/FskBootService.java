package com.fsk.framework.autoconfigure.service;

import com.fsk.framework.autoconfigure.properties.FskBootStarterProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FskBootService {

    private FskBootStarterProperties fskBootStarterProperties;

    public void bootInit(){
        System.out.println("fsk app boot init now");
    }

    public void getBootConfig(){

    }
}
