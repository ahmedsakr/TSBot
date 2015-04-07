/**
 * Copyright (c) 2015 Ahmed Sakr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tsbot.login.security;


/**
 *
 * @author ahmad sakr
 * @since March 16, 2015.
 */
public class Credential {


    private String credential;

    /**
     * Default constructor for {@link Credential}.
     *
     * @param credential the credential in question.
     */
    public Credential(String credential) {
        this.credential = credential;
    }


    /**
     * Explicitly editing the in-built method toString().
     *
     * @return the credential.
     */
    @Override
    public String toString() {
        return credential;
    }
}
