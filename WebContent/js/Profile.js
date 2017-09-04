/**
 * Created by Siperius on 2017/9/1.
 */
function m_blurUsername() {
    if (registForm.username.value.length == 0) {
        document.getElementById("m_username").style.borderColor = "red";
        document.getElementById("m_usernameWarning").style.display = "block";
        return false;
    }
    else
        return true;
}
function m_focusUsername() {
    document.getElementById("m_username").style.borderColor = "transparent";
    document.getElementById("m_usernameWarning").style.display = "none";
}
function m_blurPassword() {
    if (registForm.password.value.length < 6) {
        document.getElementById("m_password").style.borderColor = "red";
        document.getElementById("m_passwordWarning").style.display = "block";
        return false;
    }
    else
        return true;
}
function m_focusPassword() {
    document.getElementById("m_password").style.borderColor = "transparent";
    document.getElementById("m_passwordWarning").style.display = "none";
}
function m_blurNickname() {
    if (registForm.nickname.value.length == 0) {
        document.getElementById("m_nickname").style.borderColor = "red";
        document.getElementById("m_nicknameWarning").style.display = "block";
    }
}
function m_focusNickname() {
    document.getElementById("m_nickname").style.borderColor = "transparent";
    document.getElementById("m_nicknameWarning").style.display = "none";
}
function m_blurEmail() {
    if (registForm.email.value.length == 0) {
        document.getElementById("m_email").style.borderColor = "red";
        document.getElementById("m_emailWarning").style.display = "block";
        return false;
    }
    else
        return true;
}
function m_focusEmail() {
    document.getElementById("m_email").style.borderColor = "transparent";
    document.getElementById("m_emailWarning").style.display = "none";
}
function m_blurTelephone() {
    if (registForm.telephone.value.length != 11) {
        document.getElementById("m_telephone").style.borderColor = "red";
        document.getElementById("m_telephoneWarning").style.display = "block";
        return false;
    }
    else
        return true;
}
function m_focusTelephone() {
    document.getElementById("m_telephone").style.borderColor = "transparent";
    document.getElementById("m_telephoneWarning").style.display = "none";
}
function m_blurCheckcode() {
    if (registForm.checkcode.value.length == 0) {
        document.getElementById("m_checkcode").style.borderColor = "red";
        document.getElementById("m_checkcodeWarning").style.display = "block";
        return false;
    }
    else
        return true;
}
function m_focusCheckcode() {
    document.getElementById("m_checkcode").style.borderColor = "transparent";
    document.getElementById("m_checkcodeWarning").style.display = "none";
}

function m_blurCheckpassword() {
    var password1 = document.getElementById("m_password").value;
    var password2 = document.getElementById("m_checkPassword").value;
    if (password1 != password2) {
        document.getElementById("m_checkPasswordWarning").style.display = "block";
        document.getElementById("m_checkPassword").style.borderColor = "red";
    }
}

function m_focusCheckpassword() {
    document.getElementById("m_checkPassword").style.borderColor = "transparent";
    document.getElementById("m_checkPasswordWarning").style.display = "none";
}
