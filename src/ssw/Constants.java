/*
Copyright (c) 2008~2009, Justin R. Bengtson (poopshotgun@yahoo.com)
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
        this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice,
        this list of conditions and the following disclaimer in the
        documentation and/or other materials provided with the distribution.
    * Neither the name of Justin R. Bengtson nor the names of contributors may
        be used to endorse or promote products derived from this software
        without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package ssw;

public class Constants {
    // Constants for the program

    // here is the versioning and program name
    public final static String AppName = "SSW",
                        AppDescription = "Solaris Skunk Werks",
                        Version = "0.6.13",
                        AppRelease = "Beta 2",
                        OptionsFileName = "SSWoptions",
                        ImageListFileName = "S7Images",
                        LogFileName = "SSW_Log.txt",
                        HTMLTemplateName = "HTML_Template.html",
                        DEFAULT_CHASSIS = "Standard Structure",
                        DEFAULT_ENGINE = "Fusion Engine",
                        DEFAULT_GYRO = "Standard Gyro",
                        DEFAULT_COCKPIT = "Standard Cockpit",
                        DEFAULT_ENHANCEMENT = "No Enhancement",
                        DEFAULT_HEATSINK = "Single Heat Sink",
                        DEFAULT_JUMPJET = "Standard Jump Jet",
                        DEFAULT_ARMOR = "Standard Armor",
                        Solaris7URL = "http://www.solaris7.com/service/index.asp",
                        BASELOADOUT_NAME = "Base Loadout",
                        WEAPONSFILE = "weapons.dat",
                        AMMOFILE = "ammunition.dat";

    public final static int SINGLE_HEATSINK = 0,
                     DOUBLE_HEATSINK = 1,
                     DEFAULT_CTR_ARMOR_PERCENT = 25,
                     DEFAULT_STR_ARMOR_PERCENT = 25,
                     LOC_HD = 0,
                     LOC_CT = 1,
                     LOC_LT = 2,
                     LOC_RT = 3,
                     LOC_LA = 4,
                     LOC_RA = 5,
                     LOC_LL = 6,
                     LOC_RL = 7,
                     LOC_CTR = 8,
                     LOC_LTR = 9,
                     LOC_RTR = 10,
                     BF_SIZE_LIGHT = 1,
                     BF_SIZE_MEDIUM = 2,
                     BF_SIZE_HEAVY = 3,
                     BF_SIZE_ASSAULT = 4,
                     BF_SHORT = 0,
                     BF_MEDIUM = 1,
                     BF_LONG = 2,
                     BF_EXTREME = 3,
                     BF_OV = 4;


    public final static String strSTAR_LEAGUE = "Age of War/Star League",
                        strSUCCESSION = "Succession Wars",
                        strCLAN_INVASION = "Clan Invasion",
                        strDARK_AGES = "Dark Ages",
                        strALL_ERA = "Non-Canon",
                        strINNER_SPHERE = "Inner Sphere",
                        strCLAN = "Clan",
                        strMIXED = "Mixed",
                        strSINGLE_HEATSINK = "Single",
                        strDOUBLE_HEATSINK = "Double",
                        NO_IMAGE = "../Images/No_Image.png";
    public final static String[] Locs = { "Head", "Center Torso", "Left Torso",
        "Right Torso", "Left Arm", "Right Arm", "Left Leg", "Right Leg" };
}
