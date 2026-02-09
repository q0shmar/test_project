package org.example.testpages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.example.GlobalUser;
import org.example.Reader;
import org.example.enums.Role;

import static com.codeborne.selenide.Selenide.$x;

public class PortalLoginPage {
    private final SelenideElement usernameInput = $x("//input[@placeholder='Логин или почта']");
    private final SelenideElement passwordInput = $x("//input[@placeholder='Пароль']");
    private final SelenideElement submitButton = $x("//button[@type='submit']");

    private void signIn(String user, String password) {
        usernameInput.shouldBe(Condition.visible).setValue(user);
        passwordInput.shouldBe(Condition.visible).setValue(password);
        submitButton.shouldBe(Condition.visible).click();
    }


    public PortalLoginPage() {
        Selenide.open(Reader.getProperty("base.url"));
    }

    public ProjectContextPage signInRole(Role role) {
        var user = GlobalUser.builder()
                .role(role)
                .build()
              .createObject();
       signIn(user.getUsername(), user.getPassword());
        return new ProjectContextPage();
   }

 public OrdersPage signInWithProjectDef(Role role) {
      signInRole(role)
               .chooseProject();
        return new OrdersPage();
}
}
