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

package com.tsbot.management.interaction;


/**
 *
 * @author Ahmad Sakr
 * @since March 31, 2015.
 */
public class Intellect {

    private String input, output;
    private boolean contains;


    /**
     * Assigns all parameters to their corresponding ones.
     *
     * @param input the input text
     * @param contains the contains condition
     * @param output the output text
     */
    public Intellect(String input, boolean contains, String output) {
        this.input = input;
        this.output = output;
        this.contains = contains;
    }

    /**
     *
     * @return the input text.
     */
    public String getInputText() {
        return input;
    }

    /**
     *
     * @return the output text.
     */
    public String getOutputText() {
        return output;
    }


    /**
     *
     * @return the contains condition.
     */
    public boolean containsOnly() {
        return contains;
    }
}
