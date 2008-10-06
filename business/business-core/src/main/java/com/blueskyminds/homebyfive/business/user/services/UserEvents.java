package com.blueskyminds.homebyfive.business.user.services;

/**
 * Date Started: 30/04/2008
 */
public class UserEvents {

    public static final String NEW_ACCOUNT_REGISTERED = "newAccountRegistered";
    public static final String NEW_ACCOUNT_VERIFIED = "newAccountVerified";

    /** Failed to send the verification email for a new account */
    public static final String VERIFICATION_EMAIL_FAILED = "verificationEmailFailed";
}