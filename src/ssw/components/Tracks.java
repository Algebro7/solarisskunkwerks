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

/**
 *
 * @author Michael Mills
 */
public class Tracks extends MultiSlotSystem{
    private AvailableCode ISAC = new AvailableCode (false, 'C', 'D', 'E', 'E', 2400, 0, 0, "DC", "", false, false, 0, false, "", Constants.TOURNAMENT, Constants.TOURNAMENT);
    private AvailableCode CLAC = new AvailableCode (true, 'C', 'X', 'E', 'E', 2400, 0, 0, "DC", "", false, false, 0, false, "", Constants.TOURNAMENT, Constants.TOURNAMENT);

    public Tracks(Mech m, AvailableCode a){
        super (m, "Tracks", "Tracks", 0.1f, true, true, 0.0f, false, a);
        this.SetWeightBasedOnMechTonnage(true);
    }

    @Override
    public int ReportCrits(){
        if (Owner.IsQuad())
            return 4;
        return 2;
    }

    @Override
    public float GetCost() {
        return 500 * Owner.GetTonnage() * Owner.GetEngine().GetRating() / 75;
    }

    @Override
    public AvailableCode GetAvailability() {
        if( Owner.IsClan() ) {
            return CLAC;
        } else {
            return ISAC;
        }
    }

    @Override
    public boolean Place( ifLoadout l ) {
        // Place the system in the mech
        boolean placed = false;
        int increment;
        // quads have less space in the front legs, hence the check
        try {
            if( l.IsQuad() ) {
                increment = 5;
                while( placed == false ) {
                    if ( increment < 0 ) { return false; }
                    try {
                        l.AddToLA( this, increment );
                        increment--;
                        placed = true;
                    } catch ( Exception e ) {
                        increment--;
                    }
                }
                placed = false;
                increment = 5;
                while( placed == false ) {
                    if ( increment < 0 ) { return false; }
                    try {
                        l.AddToRA( this, increment );
                        increment--;
                        placed = true;
                    } catch ( Exception e ) {
                        increment--;
                    }
                }
            }
            // allocate to the (rear) legs.
            placed = false;
            increment = 5;
            while( placed == false ) {
                if ( increment < 0 ) { return false; }
                try {
                    l.AddToLL( this, increment );
                    increment--;
                    placed = true;
                } catch ( Exception e ) {
                    increment--;
                }
            }
            placed = false;
            increment = 5;
            while( placed == false ) {
                if ( increment < 0 ) { return false; }
                try {
                    l.AddToRL( this, increment );
                    increment--;
                    placed = true;
                } catch ( Exception e ) {
                    increment--;
                }
            }
        } catch ( Exception e ) {
            // something else was probably in the way.  Tell the placer
            return false;
        }

        // all went well
        return true;
    }

    @Override
    public boolean Place( ifLoadout l, LocationIndex[] Locs ) {
        try {
            if( Locs == null ) {
                return Place( l );
            }
            if( l.IsQuad() ) {
                boolean FRL = false, FLL = false, RRL = false, RLL = false;
                for( int i = 0; i < Locs.length; i++ ) {
                    if( Locs[i] != null ) {
                        l.AddTo( this, Locs[i].Location, Locs[i].Index );
                        switch( Locs[i].Location ) {
                            case Constants.LOC_RA:
                                FRL = true;
                            case Constants.LOC_LA:
                                FLL = true;
                            case Constants.LOC_RL:
                                RRL = true;
                            case Constants.LOC_LL:
                                RLL = true;
                        }
                    }
                }
                return ( FRL && FLL && RRL && RLL );
            } else {
                boolean RL = false, LL = false;
                for( int i = 0; i < Locs.length; i++ ) {
                    if( Locs[i] != null ) {
                        l.AddTo( this, Locs[i].Location, Locs[i].Index );
                        switch( Locs[i].Location ) {
                            case Constants.LOC_RL:
                                RL = true;
                            case Constants.LOC_LL:
                                LL = true;
                        }
                    }
                }
                return ( RL && LL );
            }
        } catch ( Exception e ) {
            // something else was probably in the way.  Tell the placer
            return false;
        }
    }
}
