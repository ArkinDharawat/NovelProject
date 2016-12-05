package com.hfad.novelproject;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SavedStoriesTest {

    @Rule
    public ActivityTestRule<StartLoginActivity> mActivityTestRule = new ActivityTestRule<>(StartLoginActivity.class);

    @Test
    public void startLoginActivityTest2() {
        ViewInteraction appCompatEditText = onView(
                withId(R.id.editText2));
        appCompatEditText.perform(scrollTo(), replaceText("+16509952455"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button), withText("Sign In or Register"),
                        withParent(allOf(withId(R.id.email_login_form),
                                withParent(withId(R.id.login_form))))));
        appCompatButton.perform(scrollTo(), click());

        pressBack();

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.button3), withText("Saved Stories"),
                        withParent(allOf(withId(R.id.activity_detail),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textView), withText("Whispering Door"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.saved_list),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Whispering Door")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textView2), withText("horror"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.saved_list),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("horror")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.textView2), withText("horror"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.saved_list),
                                        0),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("horror")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
