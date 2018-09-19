package com.accedia.noto;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import com.accedia.noto.dagger.Dagger;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class UITests {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    private App getApp() {
        return (App) InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getApplicationContext();
    }

    @Test
    public void fabWorks() {
        Dagger.init(getApp());
        activityRule.launchActivity(null);
        Intents.init();
        onView(withId(R.id.fab_add_word)).perform(click());
        intended(hasComponent(NewWordActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void addingNewWordWorks() {
        String newWord = "quixotic";
        Dagger.init(getApp());
        activityRule.launchActivity(null);
        onView(withId(R.id.fab_add_word)).perform(click());
        onView(withId(R.id.edit_word)).perform(
                typeText(newWord), closeSoftKeyboard());
        onView(withId(R.id.button_save)).perform(click());
        onView(withId(R.id.recyclerview))
                .perform(scrollTo(hasDescendant(withText(newWord))));
        onView(withText(newWord)).check(matches(isDisplayed()));
    }

}
