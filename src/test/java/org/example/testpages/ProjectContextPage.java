package org.example.testpages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Conditional;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class ProjectContextPage {
    private final SelenideElement orgSelectButton = $x("(//button[@data-qa-id='ContextModalHeader_Button__285e38'])");
    private final SelenideElement orgSelectValueBox = $x("//li[contains(@class, 'ant-dropdown-menu-item')]//div[contains(text(), 'Облачные решения (Демо)')]");
    private final SelenideElement contextButton = $x("(//aside//following::div[contains(@class, 'menuIconWrapper')])[1]");
    private final SelenideElement selectFolder = $x("//div[@class='treeNode__title__cIc5au' and text()='qa_test_Mikhail']");
    private final SelenideElement selectProject = $x("//span[contains(@class, 'treeItem__text__lQH0N1') and text()='qa_mv1']");

    public ProjectContextPage goToContextPage() {
        contextButton.shouldBe(Condition.appear).shouldBe(Condition.visible).scrollTo().click();
        return this;
    }

    public ProjectContextPage selectOrganization(){
    orgSelectButton.shouldBe(Condition.visible).scrollTo().click();
    orgSelectValueBox.shouldBe(Condition.visible).click();
    return this;
    }

    public OrdersPage selectProject(){
        selectFolder.shouldBe(Condition.appear).shouldBe(Condition.visible).scrollTo().click();
        selectProject.shouldBe(Condition.visible).scrollTo().click();
        return new OrdersPage();
    }

    public ProjectContextPage chooseProject(){
        goToContextPage()
                .selectOrganization()
                .selectProject();
        return this;
    }
}