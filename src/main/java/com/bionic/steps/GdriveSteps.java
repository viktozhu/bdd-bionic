package com.bionic.steps;

import com.bionic.helpers.FileHelper;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

/**
 * Created by bdd on 7/30/15.
 */
public class GdriveSteps extends ScenarioSteps{
    @Step
    public void createFile(String name, int size){
        FileHelper.createTestFile(name, size);
    }
}
