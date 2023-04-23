package com.codeup.gitfitcrew.fitconnect.controllers;

import com.codeup.gitfitcrew.fitconnect.models.User;
import com.codeup.gitfitcrew.fitconnect.services.UserServices;
import com.codeup.gitfitcrew.fitconnect.services.Utility;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

@Controller
public class ForgotPasswordController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserServices userService;


    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgot_password_form";

    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check your spam folder.");

        } catch (UsernameNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }

        return "forgot_password_form";
    }

    public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("FitConnectCorp@gmail.com", "FitConnect Support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model
            , @RequestParam(required = false) String paramType
            , @RequestParam(required = false) String msg) {
        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if(paramType != null) {
            System.out.println(paramType);
            model.addAttribute(paramType, msg);
        }
        if(msg != null) {
            System.out.println(msg);
        }

        if (user == null) {
            String message = "invalid token";
            model.addAttribute("message", message);
            return message;
        }

        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (user == null) {
            String message = "invalid token";
            model.addAttribute("message", message);
            return message;
        } else {
            user.setPassword(password);
            Pattern upperCase = Pattern.compile("[A-Z ]");
            Pattern numbers = Pattern.compile("[0-9 ]");
            Pattern specialChar = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);

            if (!upperCase.matcher(user.getPassword()).find()) {
                return "redirect:/reset_password?token=" + token + "&paramType=uppercase&msg=password must have an uppercase letter";
            }
            if (!numbers.matcher(user.getPassword()).find()) {
                model.addAttribute("numbers", "password must have a number");
                return "redirect:/reset_password?token=" + token + "&paramType=numbers&msg=password must have a number";
            }
            if (!specialChar.matcher(user.getPassword()).find()) {
                model.addAttribute("special", "password must have a special character");
                return "redirect:/reset_password?token=" + token + "&paramType=special&msg=password must have a special character";
            }
            if (!(user.getPassword().length() >= 8)) {
                model.addAttribute("length", "password must be at least  8 characters");
                return "redirect:/reset_password?token=" + token + "&paramType=length&msg=password must be at least  8 characters";
            }
            userService.updatePassword(user, password);

        }

        return "login";
    }
}
