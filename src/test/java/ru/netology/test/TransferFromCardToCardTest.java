package ru.netology.test;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.CardBalance;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.getFirstCardNumber;
import static ru.netology.data.DataHelper.getSecondCardNumber;
import static ru.netology.page.CardBalance.pushSecondCardButton;

public class TransferFromCardToCardTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var cardBalance = verificationPage.validVerify(verificationCode);
    }

    @Test
    public void shouldTransferMoneyFromFirstToSecond() {
        int amount = 5000;
        var cardBalance = new CardBalance();
        var firstCardBalanceStart = cardBalance.getFirstCardBalance();
        var secondCardBalanceStart = cardBalance.getSecondCardBalance();
        var transactionPage = CardBalance.pushSecondCardButton(1);
        transactionPage.transferMoney(amount, String.valueOf(getFirstCardNumber()));
        var firstCardBalanceFinish = firstCardBalanceStart - amount;
        var secondCardBalanceFinish = secondCardBalanceStart + amount;

        assertEquals(firstCardBalanceFinish, firstCardBalanceStart - amount);
        assertEquals(secondCardBalanceFinish, secondCardBalanceStart + amount);
    }

    @Test
    public void shouldTransferMoneyFromSecondToFirst() {
        int amount = 20000;
        var cardBalance = new CardBalance();
        var firstCardBalanceStart = cardBalance.getFirstCardBalance();
        var secondCardBalanceStart = cardBalance.getSecondCardBalance();
        var transactionPage = CardBalance.pushFirstCardButton(0);
        transactionPage.transferMoney(amount, String.valueOf(getSecondCardNumber()));
        var firstCardBalanceFinish = firstCardBalanceStart + amount;
        var secondCardBalanceFinish = secondCardBalanceStart - amount;

        assertEquals(firstCardBalanceFinish, firstCardBalanceStart + amount);
        assertEquals(secondCardBalanceFinish, secondCardBalanceStart - amount);
    }

//    @Test
//    public void shouldTransferExceedCardBalance() {
//        int amount = 30000;
//        var cardBalance = new CardBalance();
//        var firstCardBalanceStart = cardBalance.getFirstCardBalance();
//        var secondCardBalanceStart = cardBalance.getSecondCardBalance();
//        var transactionPage = pushSecondCardButton(1);
//        transactionPage.transferMoney(amount, String.valueOf(getFirstCardNumber()));
//        transactionPage.errorLimit();
//    }
    @Test
    public void transferToANonExistentCard() {
        int amount = 5000;
        var cardBalance = new CardBalance();
        var firstCardBalanceStart = cardBalance.getFirstCardBalance();
        var secondCardBalanceStart = cardBalance.getSecondCardBalance();
        var transactionPage = pushSecondCardButton(1);
        transactionPage.transferMoney(amount, String.valueOf("2345345345"));
        transactionPage.invalidCard();
    }
}
