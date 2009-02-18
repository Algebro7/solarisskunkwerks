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

public class ActuatorSet {
    // this is a little class to encapsulate all the actuators on a mech.  It's
    // here to keep things clean within the mech.
    private final static AvailableCode AC = new AvailableCode( false, 'C', 'C', 'C', 'C',
        2300, 0, 0, "TH", "", false, false );
    private boolean LHInstalled = true,
                    RHInstalled = true,
                    LLAInstalled = true,
                    RLAInstalled = true,
                    LockedLeft = false,
                    LockedRight = false;
    private static Actuator LeftHip,
                            RightHip,
                            LeftFrontHip,
                            RightFrontHip,
                            LeftLowerLeg,
                            RightLowerLeg,
                            LeftFrontLowerLeg,
                            RightFrontLowerLeg,
                            LeftUpperLeg,
                            RightUpperLeg,
                            LeftFrontUpperLeg,
                            RightFrontUpperLeg,
                            LeftFoot,
                            RightFoot,
                            LeftFrontFoot,
                            RightFrontFoot,
                            LeftShoulder,
                            RightShoulder,
                            LeftUpperArm,
                            RightUpperArm;
    private Actuator LeftLowerArm,
                     LeftHand,
                     RightLowerArm,
                     RightHand;
    private ifLoadout Owner;
    
    public ActuatorSet( ifLoadout l, Mech m ) {
        Owner = l;
        LeftHip = new Actuator( "Hip", "Hip", true, AC, 0.0f, m );
        RightHip = new Actuator( "Hip", "Hip", true, AC, 0.0f, m );
        LeftFrontHip = new Actuator( "Hip", "Hip", true, AC, 0.0f, m );
        RightFrontHip = new Actuator( "Hip", "Hip", true, AC, 0.0f, m );
        LeftLowerLeg = new Actuator( "Lower Leg", "Lower Leg Actuator", true, AC, 80.0f, m );
        RightLowerLeg = new Actuator( "Lower Leg", "Lower Leg Actuator", true, AC, 80.0f, m );
        LeftFrontLowerLeg = new Actuator( "Lower Leg", "Lower Leg Actuator", true, AC, 80.0f, m );
        RightFrontLowerLeg = new Actuator( "Lower Leg", "Lower Leg Actuator", true, AC, 80.0f, m );
        LeftUpperLeg = new Actuator( "Upper Leg", "Upper Leg Actuator", true, AC, 150.0f, m );
        RightUpperLeg = new Actuator( "Upper Leg", "Upper Leg Actuator", true, AC, 150.0f, m );
        LeftFrontUpperLeg = new Actuator( "Upper Leg", "Upper Leg Actuator", true, AC, 150.0f, m );
        RightFrontUpperLeg = new Actuator( "Upper Leg", "Upper Leg Actuator", true, AC, 150.0f, m );
        LeftFoot = new Actuator( "Foot", "Foot Actuator", true, AC, 120.0f, m );
        RightFoot = new Actuator( "Foot", "Foot Actuator", true, AC, 120.0f, m );
        LeftFrontFoot = new Actuator( "Foot", "Foot Actuator", true, AC, 120.0f, m );
        RightFrontFoot = new Actuator( "Foot", "Foot Actuator", true, AC, 120.0f, m );
        LeftShoulder = new Actuator( "Shoulder", "Shoulder", true, AC, 0.0f, m );
        RightShoulder = new Actuator( "Shoulder", "Shoulder", true, AC, 0.0f, m );
        LeftUpperArm = new Actuator( "Upper Arm", "Upper Arm Actuator", true, AC, 100.0f, m );
        RightUpperArm = new Actuator( "Upper Arm", "Upper Arm Actuator", true, AC, 100.0f, m );
        LeftLowerArm = new Actuator( "Lower Arm", "Lower Arm Actuator", true, AC, 50.0f, m );
        LeftHand = new Actuator( "Hand", "Hand Actuator", true, AC, 80.0f, m );
        RightLowerArm = new Actuator( "Lower Arm", "Lower Arm Actuator", true, AC, 50.0f, m );
        RightHand = new Actuator( "Hand", "Hand Actuator", true, AC, 80.0f, m );
    }

    public void RemoveAll() {
        Owner.Remove( LeftHip );
        Owner.Remove( RightHip );
        Owner.Remove( LeftLowerLeg );
        Owner.Remove( RightLowerLeg );
        Owner.Remove( LeftUpperLeg );
        Owner.Remove( RightUpperLeg );
        Owner.Remove( LeftFoot );
        Owner.Remove( RightFoot );
        if(  Owner.IsQuad() ) {
            Owner.Remove( LeftFrontHip );
            Owner.Remove( RightFrontHip );
            Owner.Remove( LeftFrontLowerLeg );
            Owner.Remove( RightFrontLowerLeg );
            Owner.Remove( LeftFrontUpperLeg );
            Owner.Remove( RightFrontUpperLeg );
            Owner.Remove( LeftFrontFoot );
            Owner.Remove( RightFrontFoot );
        } else {
            Owner.Remove( LeftShoulder );
            Owner.Remove( RightShoulder );
            Owner.Remove( LeftUpperArm );
            Owner.Remove( RightUpperArm );
            Owner.Remove(RightLowerArm);
            Owner.Remove(LeftLowerArm);
            Owner.Remove(RightHand);
            Owner.Remove(LeftHand);
        }
    }

    public boolean LockedLeft() {
        return LockedLeft;
    }

    public boolean LockedRight() {
        return LockedRight;
    }

    public void SetLockedLeft( boolean b ) {
        LockedLeft = b;
    }

    public void SetLockedRight( boolean b ) {
        LockedRight = b;
    }

    public void RemoveRightHand() {
        // removes the right hand actuator
        if( Owner.IsQuad() ) { return; }
        if( Owner.GetMech().IsOmnimech() ) {
            if( LockedRight ) {
                RHInstalled = true;
                RLAInstalled = true;
                return;
            }
        }
        Owner.Remove( RightHand );
        RHInstalled = false;
    }

    public void RemoveLeftHand() {
        // removes the left hand actuator
        if( Owner.IsQuad() ) { return; }
        if( Owner.GetMech().IsOmnimech() ) {
            if( LockedLeft ) {
                LHInstalled = true;
                LLAInstalled = true;
                return;
            }
        }
        Owner.Remove( LeftHand );
        LHInstalled = false;
    }

    public void RemoveRightLowerArm() {
        // removes the right lower arm actuator
        // ensure the right hand actuator is removed as well
        if( Owner.IsQuad() ) { return; }
        if( Owner.GetMech().IsOmnimech() ) {
            if( LockedRight ) {
                RHInstalled = true;
                RLAInstalled = true;
                return;
            }
        }
        Owner.Remove( RightHand );
        Owner.Remove( RightLowerArm );
        RHInstalled = false;
        RLAInstalled = false;
    }

    public void RemoveLeftLowerArm() {
        // removes the left lower arm actuator
        // ensure the left hand actuator is removed as well
        if( Owner.IsQuad() ) { return; }
        if( Owner.GetMech().IsOmnimech() ) {
            if( LockedLeft ) {
                LHInstalled = true;
                LLAInstalled = true;
                return;
            }
        }
        Owner.Remove( LeftHand );
        Owner.Remove( LeftLowerArm );
        LHInstalled = false;
        LLAInstalled = false;
    }

    public boolean AddRightHand() {
        // add the right hand back in.
        if( Owner.IsQuad() ) { return true; }

        // check and see if the lower arm actuator is there.  if not, add it in.
        if( ! Owner.IsAllocated( RightLowerArm ) ) {
            try {
                Owner.AddToRA( RightLowerArm, 2 );
            } catch ( Exception e ) {
                // Actuators always take precedence.  Let the mech know.
                return false;
            }
        }

        RLAInstalled = true;

        // now add the hand back in
        try {
            Owner.AddToRA( RightHand, 3 );
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        RHInstalled = true;

        // everything worked fine.
        return true;
    }

    public boolean AddLeftHand() {
        // add the right hand back in.
        if( Owner.IsQuad() ) { return true; }

        // check and see if the lower arm actuator is there.  if not, add it in.
        if( ! Owner.IsAllocated( LeftLowerArm ) ) {
            try {
                Owner.AddToLA( LeftLowerArm, 2 );
            } catch ( Exception e ) {
                // Actuators always take precedence.  Let the mech know.
                return false;
            }
        }

        LLAInstalled = true;

        // now add the hand back in
        try {
            Owner.AddToLA( LeftHand, 3 );
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        LHInstalled = true;

        // everything worked fine.
        return true;
    }

    public boolean AddRightLowerArm() {
        // add the right lower arm back in.
        if( Owner.IsQuad() ) { return true; }

        try {
            Owner.AddToRA( RightLowerArm, 2 );
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        RLAInstalled = true;

        // everything worked fine.
        return true;
    }

    public boolean AddLeftLowerArm() {
        // add the right lower arm back in.
        if( Owner.IsQuad() ) { return true; }

        try {
            Owner.AddToLA( LeftLowerArm, 2 );
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        LLAInstalled = true;

        // everything worked fine.
        return true;
    }

    public boolean PlaceActuators() {
        // we're ignoring the normal methods so we don't have to create eight
        // seperate actuator classes.  This is a manual placement.
        try {
            Owner.AddToLL( LeftHip, 0 );
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        try {
            Owner.AddToLL( LeftUpperLeg, 1 );
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        try {
            Owner.AddToLL( LeftLowerLeg, 2 );
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        try {
            Owner.AddToLL( LeftFoot, 3 );
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        try {
            Owner.AddToRL( RightHip, 0 );
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        try {
            Owner.AddToRL( RightUpperLeg, 1 );
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        try {
            Owner.AddToRL( RightLowerLeg, 2 );
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        try {
            Owner.AddToRL( RightFoot, 3 );
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        try {
            if( Owner.IsQuad() ) {
                Owner.AddToLA( LeftFrontHip, 0 );
            } else {
                Owner.AddToLA( LeftShoulder, 0 );
            }
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        try {
            if( Owner.IsQuad() ) {
                Owner.AddToLA( LeftFrontUpperLeg, 1 );
            } else {
                Owner.AddToLA( LeftUpperArm, 1 );
            }
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        try {
            if( Owner.IsQuad() ) {
                LLAInstalled = true;
                Owner.AddToLA( LeftFrontLowerLeg, 2 );
            } else {
                if( LLAInstalled ) {
                    Owner.AddToLA( LeftLowerArm, 2 );
                }
            }
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        try {
            if( Owner.IsQuad() ) {
                LHInstalled = true;
                Owner.AddToLA( LeftFrontFoot, 3 );
            } else {
                if( LHInstalled ) {
                    Owner.AddToLA( LeftHand, 3 );
                }
            }
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        try {
            if( Owner.IsQuad() ) {
                Owner.AddToRA( RightFrontHip, 0 );
            } else {
                Owner.AddToRA( RightShoulder, 0 );
            }
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        try {
            if( Owner.IsQuad() ) {
                Owner.AddToRA( RightFrontUpperLeg, 1 );
            } else {
                Owner.AddToRA( RightUpperArm, 1 );
            }
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        try {
            if( Owner.IsQuad() ) {
                RLAInstalled = true;
                Owner.AddToRA( RightFrontLowerLeg, 2 );
            } else {
                if( RLAInstalled ) {
                    Owner.AddToRA( RightLowerArm, 2 );
                }
            }
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        try {
            if( Owner.IsQuad() ) {
                RHInstalled = true;
                Owner.AddToRA( RightFrontFoot, 3 );
            } else {
                if( RHInstalled ) {
                    Owner.AddToRA( RightHand, 3 );
                }
            }
        } catch ( Exception e ) {
            // Actuators always take precedence.  Let the mech know.
            return false;
        }

        // everything worked out fine.
        return true;
    }

    public float GetCost() {
        // NOTE: Hips and shoulders do not have a cost, so we skip them
        float result = 0.0f;
        if( Owner.IsQuad() ) {
            // This is easy since no actuators will ever be removed
            result += LeftHip.GetCost();
            result += RightHip.GetCost();
            result += LeftUpperLeg.GetCost();
            result += LeftLowerLeg.GetCost();
            result += LeftFoot.GetCost();
            result += RightUpperLeg.GetCost();
            result += RightLowerLeg.GetCost();
            result += RightFoot.GetCost();
            result += LeftFrontHip.GetCost();
            result += RightFrontHip.GetCost();
            result += LeftFrontUpperLeg.GetCost();
            result += LeftFrontLowerLeg.GetCost();
            result += LeftFrontFoot.GetCost();
            result += RightFrontUpperLeg.GetCost();
            result += RightFrontLowerLeg.GetCost();
            result += RightFrontFoot.GetCost();
        } else {
            // get the shoulders and leg actuators first
            result += LeftShoulder.GetCost();
            result += RightShoulder.GetCost();
            result += LeftUpperArm.GetCost();
            result += RightUpperArm.GetCost();
            result += LeftHip.GetCost();
            result += RightHip.GetCost();
            result += LeftUpperLeg.GetCost();
            result += RightUpperLeg.GetCost();
            result += LeftLowerLeg.GetCost();
            result += RightLowerLeg.GetCost();
            result += LeftFoot.GetCost();
            result += RightFoot.GetCost();

            // now, for each actuator that may be missing
            if( RLAInstalled ) {
                result += RightLowerArm.GetCost();
            }
            if( LLAInstalled ) {
                result += LeftLowerArm.GetCost();
            }
            if( RHInstalled ) {
                result += RightHand.GetCost();
            }
            if( LHInstalled ) {
                result += LeftHand.GetCost();
            }
        }
        return result;
    }

    public float GetTonnage() {
        // gets the tonnage of the actuators.  Mostly added for armored support
        float result = 0.0f;
        if( Owner.IsQuad() ) {
            // This is easy since no actuators will ever be removed
            result += LeftHip.GetTonnage();
            result += RightHip.GetTonnage();
            result += LeftUpperLeg.GetTonnage();
            result += LeftLowerLeg.GetTonnage();
            result += LeftFoot.GetTonnage();
            result += RightUpperLeg.GetTonnage();
            result += RightLowerLeg.GetTonnage();
            result += RightFoot.GetTonnage();
            result += LeftFrontHip.GetTonnage();
            result += RightFrontHip.GetTonnage();
            result += LeftFrontUpperLeg.GetTonnage();
            result += LeftFrontLowerLeg.GetTonnage();
            result += LeftFrontFoot.GetTonnage();
            result += RightFrontUpperLeg.GetTonnage();
            result += RightFrontLowerLeg.GetTonnage();
            result += RightFrontFoot.GetTonnage();
        } else {
            // get the shoulders and leg actuators first
            result += LeftShoulder.GetTonnage();
            result += RightShoulder.GetTonnage();
            result += LeftUpperArm.GetTonnage();
            result += RightUpperArm.GetTonnage();
            result += LeftHip.GetTonnage();
            result += RightHip.GetTonnage();
            result += LeftUpperLeg.GetTonnage();
            result += RightUpperLeg.GetTonnage();
            result += LeftLowerLeg.GetTonnage();
            result += RightLowerLeg.GetTonnage();
            result += LeftFoot.GetTonnage();
            result += RightFoot.GetTonnage();

            // now, for each actuator that may be missing
            if( RLAInstalled ) {
                result += RightLowerArm.GetTonnage();
            }
            if( LLAInstalled ) {
                result += LeftLowerArm.GetTonnage();
            }
            if( RHInstalled ) {
                result += RightHand.GetTonnage();
            }
            if( LHInstalled ) {
                result += LeftHand.GetTonnage();
            }
        }
        return result;
    }

    public float GetOffensiveBV() {
        return 0.0f;
    }

    public float GetCurOffensiveBV( boolean UseRear ) {
        return 0.0f;
    }

    public float GetDefensiveBV() {
        // gets the BV of the actuators.  Mostly added for armored support
        float result = 0.0f;
        if( Owner.IsQuad() ) {
            // This is easy since no actuators will ever be removed
            result += LeftHip.GetDefensiveBV();
            result += RightHip.GetDefensiveBV();
            result += LeftUpperLeg.GetDefensiveBV();
            result += LeftLowerLeg.GetDefensiveBV();
            result += LeftFoot.GetDefensiveBV();
            result += RightUpperLeg.GetDefensiveBV();
            result += RightLowerLeg.GetDefensiveBV();
            result += RightFoot.GetDefensiveBV();
            result += LeftFrontHip.GetDefensiveBV();
            result += RightFrontHip.GetDefensiveBV();
            result += LeftFrontUpperLeg.GetDefensiveBV();
            result += LeftFrontLowerLeg.GetDefensiveBV();
            result += LeftFrontFoot.GetDefensiveBV();
            result += RightFrontUpperLeg.GetDefensiveBV();
            result += RightFrontLowerLeg.GetDefensiveBV();
            result += RightFrontFoot.GetDefensiveBV();
        } else {
            // get the shoulders and leg actuators first
            result += LeftShoulder.GetDefensiveBV();
            result += RightShoulder.GetDefensiveBV();
            result += LeftUpperArm.GetDefensiveBV();
            result += RightUpperArm.GetDefensiveBV();
            result += LeftHip.GetDefensiveBV();
            result += RightHip.GetDefensiveBV();
            result += LeftUpperLeg.GetDefensiveBV();
            result += RightUpperLeg.GetDefensiveBV();
            result += LeftLowerLeg.GetDefensiveBV();
            result += RightLowerLeg.GetDefensiveBV();
            result += LeftFoot.GetDefensiveBV();
            result += RightFoot.GetDefensiveBV();

            // now, for each actuator that may be missing
            if( RLAInstalled ) {
                result += RightLowerArm.GetDefensiveBV();
            }
            if( LLAInstalled ) {
                result += LeftLowerArm.GetDefensiveBV();
            }
            if( RHInstalled ) {
                result += RightHand.GetDefensiveBV();
            }
            if( LHInstalled ) {
                result += LeftHand.GetDefensiveBV();
            }
        }
        return result;
    }

    public boolean LeftHandInstalled() {
        return LHInstalled;
    }

    public boolean RightHandInstalled() {
        return RHInstalled;
    }

    public boolean LeftLowerInstalled() {
        return LLAInstalled;
    }

    public boolean RightLowerInstalled() {
        return RLAInstalled;
    }

    public AvailableCode GetAvailability() {
        // returns an availablecode for all actuators
        AvailableCode retval = new AvailableCode( Owner.GetMech().IsClan(), 'C', 'C', 'C', 'C', 2300, 0, 0, "TH", "", false, false );
        if( Owner.IsQuad() ) {
            // This is easy since no actuators will ever be removed
            retval.Combine( LeftHip.GetAvailability() );
            retval.Combine( RightHip.GetAvailability() );
            retval.Combine( LeftUpperLeg.GetAvailability() );
            retval.Combine( LeftLowerLeg.GetAvailability() );
            retval.Combine( LeftFoot.GetAvailability() );
            retval.Combine( RightUpperLeg.GetAvailability() );
            retval.Combine( RightLowerLeg.GetAvailability() );
            retval.Combine( RightFoot.GetAvailability() );
            retval.Combine( LeftFrontHip.GetAvailability() );
            retval.Combine( RightFrontHip.GetAvailability() );
            retval.Combine( LeftFrontUpperLeg.GetAvailability() );
            retval.Combine( LeftFrontLowerLeg.GetAvailability() );
            retval.Combine( LeftFrontFoot.GetAvailability() );
            retval.Combine( RightFrontUpperLeg.GetAvailability() );
            retval.Combine( RightFrontLowerLeg.GetAvailability() );
            retval.Combine( RightFrontFoot.GetAvailability() );
        } else {
            // get the shoulders and leg actuators first
            retval.Combine( LeftShoulder.GetAvailability() );
            retval.Combine( RightShoulder.GetAvailability() );
            retval.Combine( LeftUpperArm.GetAvailability() );
            retval.Combine( RightUpperArm.GetAvailability() );
            retval.Combine( LeftHip.GetAvailability() );
            retval.Combine( RightHip.GetAvailability() );
            retval.Combine( LeftUpperLeg.GetAvailability() );
            retval.Combine( RightUpperLeg.GetAvailability() );
            retval.Combine( LeftLowerLeg.GetAvailability() );
            retval.Combine( RightLowerLeg.GetAvailability() );
            retval.Combine( LeftFoot.GetAvailability() );
            retval.Combine( RightFoot.GetAvailability() );

            // now, for each actuator that may be missing
            if( RLAInstalled ) {
                retval.Combine( RightLowerArm.GetAvailability() );
            }
            if( LLAInstalled ) {
                retval.Combine( LeftLowerArm.GetAvailability() );
            }
            if( RHInstalled ) {
                retval.Combine( RightHand.GetAvailability() );
            }
            if( LHInstalled ) {
                retval.Combine( LeftHand.GetAvailability() );
            }
        }
        return retval;
    }

    @Override
    public String toString() {
        return "Actuator Set";
    }
}
