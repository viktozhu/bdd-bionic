package com.bionic.jbehave;

import net.thucydides.core.annotations.Steps;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import com.bionic.steps.EndUserSteps;
import org.jbehave.core.model.ExamplesTable;

import java.util.Map;

public class DefinitionSteps {

    @Steps
    EndUserSteps endUser;

    @Given("the user is on the Wikionary home page")
    public void givenTheUserIsOnTheWikionaryHomePage() {
        endUser.is_the_home_page();
    }

    @When("the user looks up the definition of the word '$word'")
    public void whenTheUserLooksUpTheDefinitionOf(String word) {
        endUser.looks_for(word);
    }

    @Then("they should see the definition '$definition'")
    public void thenTheyShouldSeeADefinitionContainingTheWords(String definition) {
        endUser.should_see_definition(definition);
    }

    @Given("user logged in as '$user'")
    public void login(String user){
    }

    @When("I update story description, set name=<storyName>, description=<storyDescription>")
    public void updateStory(@Named("storyName")String name, @Named("storyDescription")String description){
    }

    @Then("story board contains:$table")
    public void validateStoryBoard(ExamplesTable table){
        for (Map<String,String> row : table.getRows()) {
            String name = row.get("story");
            String description = row.get("description");
        }
    }

}
