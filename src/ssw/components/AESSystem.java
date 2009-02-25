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

package ssw.components;

import ssw.Constants;

public class AESSystem extends abPlaceable {
    AvailableCode ISAC = new AvailableCode( false, 'E', 'X', 'X', 'F', 3070, 0, 0, "KH", "", false, false, 3067, true, "BC", Constants.EXPERIMENTAL, Constants.EXPERIMENTAL );
    AvailableCode CLAC = new AvailableCode( true, 'E', 'X', 'X', 'F', 3070, 0, 0, "WD", "", false, false, 3067, true, "WD", Constants.EXPERIMENTAL, Constants.EXPERIMENTAL );
    private Mech Owner;
    private boolean LegSystem;
    private static MechModifier LegMod = new MechModifier( 0, 0, 0, 0, 0, -2, 0, 0.0f, 0.0f, 0.0f, 0.0f, false );

    public AESSystem( Mech m, boolean legs ) {
        Owner = m;
        LegSystem = legs;
        SetExclusions( new Exclusion( new String[] { "Targeting Computer", "MASC", "TSM" }, "A.E.S." ) );
    }

    @Override
    public String GetCritName() {
        return "A.E.S.";
    }

    @Override
    public String GetMMName(boolean UseRear) {
        if( Owner.IsClan() ) {
            return "CLAES";
        } else {
            return "ISAES";
        }
    }

    @Override
    public int NumCrits() {
        int MechTons = Owner.GetTonnage();
        if( MechTons < 40 ) {
            return 1;
        } else if( MechTons > 35 && MechTons < 60 ) {
            return 2;
        } else if( MechTons > 55 && MechTons < 80 ) {
            return 3;
        } else {
            return 4;
        }
    }

    @Override
    public float GetTonnage() {
        float retval = 0.0f;
        if( IsArmored() ) {
            retval += NumCrits() * 0.5f;
        }
        if( Owner.IsQuad() ) {
            retval += ((int) ( Math.ceil( Owner.GetTonnage() * 0.02f * 2.0f ))) * 0.5f;
        } else {
            retval += ((int) ( Math.ceil( Owner.GetTonnage() * 0.02857f * 2.0f ))) * 0.5f;
        }
        return retval;
    }

    @Override
    public float GetCost() {
        float retval = 0.0f;
        if( IsArmored() ) {
            retval += NumCrits() * 150000.0f;
        }
        if( LegSystem ) {
            retval += Owner.GetTonnage() * 700.0f;
        } else {
            retval += Owner.GetTonnage() * 500.0f;
        }
        return retval;
    }

    @Override
    public float GetOffensiveBV() {
        // AES modifies BV, but doesn't have one of its own
        return 0.0f;
    }

    @Override
    public float GetCurOffensiveBV( boolean UseRear, boolean UseTC, boolean UseAES ) {
        // AES modifies BV, but doesn't have one of its own
        return 0.0f;
    }

    @Override
    public float GetDefensiveBV() {
        if( IsArmored() ) {
            return 5.0f * NumCrits();
        } else {
            return 0.0f;
        }
    }

    @Override
    public boolean LocationLocked() {
        return true;
    }

    @Override
    public void AddMechModifier(MechModifier m) {
        // do nothing here, we provide our own.
    }

    @Override
    public MechModifier GetMechModifier() {
        if( LegSystem ) {
            return LegMod;
        } else {
            return null;
        }
    }

    @Override
    public boolean CoreComponent() {
        return true;
    }

    @Override
    public AvailableCode GetAvailability() {
        if( Owner.IsClan() ) {
            return CLAC;
        } else {
            return ISAC;
        }
    }
}