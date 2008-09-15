package com.blueskyminds.enterprise.region.postcode;

/**
 * Date Started: 8/07/2007
 * <p/>
 * History:
 */
public class PostCodeFactory {

    public PostCodeHandle createPostCode(String postCodeValue) {
        PostCode postCode = new PostCode(postCodeValue);
        PostCodeHandle postCodeHandle = new PostCodeHandle(postCodeValue, postCode);
        return postCodeHandle;
    }
}
