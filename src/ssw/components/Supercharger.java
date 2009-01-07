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

public class Supercharger extends abPlaceable {
    private ifLoadout Owner;
    private AvailableCode ISAC = new AvailableCode( false, 'C', 'F', 'F', 'F', 1950, 0, 0, "ES", "", false, false, 0, false, "", Constants.EXPERIMENTAL, Constants.EXPERIMENTAL ),
                          CLAC = new AvailableCode( true, 'C', 'F', 'F', 'F', 1950, 0, 0, "ES", "", false, false, 0, false, "", Constants.EXPERIMENTAL, Constants.EXPERIMENTAL );

    public Supercharger( ifLoadout l ) {
        Owner = l;
        AddMechModifier( new MechModifier( 0, 0, 0, 0.5f, 0, 0, 0, 0.0f, 0.0f, 0.0f, 0.0f, true ) );
    }

    @Override
    public String GetCritName() {
        return "Supercharger";
    }

    @Override
    public String GetMMName(boolean UseRear) {
        if( Owner.GetMech().IsClan() ) {
            return "CL Super Charger";
        } else {
            return "IS Super Charger";
        }
    }

    @Override
    public int NumCrits() {
        return 1;
    }

    @Override
    public float GetTonnage() {
        float retval = ((int) ( Math.ceil( Owner.GetMech().GetEngine().GetTonnage() * 0.1f * 2 ))) * 0.5f;
        if( IsArmored() ) {
            retval += 0.5f;
        }
        return retval;
    }

    @Override
    public float GetCost() {
        float retval = Owner.GetMech().GetEngine().GetRating() * 10000.0f;
        if( IsArmored() ) {
            retval += 150000.f;
        }
        return retval;
    }

    @Override
    public float GetOffensiveBV() {
        return 0.0f;
    }

    @Override
    public float GetCurOffensiveBV(boolean UseRear) {
        return 0.0f;
    }

    @Override
    public float GetDefensiveBV() {
        if( IsArmored() ) {
            return 5.0f;
        } else {
            return 0.0f;
        }
    }

    @Override
    public boolean LocationLocked() {
        return true;
    }

    @Override
    public boolean LocationLinked() {
        return true;
    }

    @Override
    public boolean CoreComponent() {
        return true;
    }

    @Override
    public AvailableCode GetAvailability() {
        if( Owner.GetMech().IsClan() ) {
            return CLAC;
        } else {
            return ISAC;
        }
    }
}
