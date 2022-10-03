bnb.scene.enableRecognizerFeature(bnb.FeatureID.BROWS_CORRECTION);
function Effect() {
    var self = this;

    this.init = function() {
        Api.meshfxMsg("spawn", 0, 0, "!glfx_FACE");
        Api.meshfxMsg("spawn", 2, 0, "eyelash.bsm2");
        Api.meshfxMsg("spawn", 3, 0, "quad.bsm2");

        //Api.meshfxMsg("spawn", 4, 0, "GlosE.bsm2");

        Api.meshfxMsg("shaderVec4", 0, 1, "0.81 0.48 0.48 0.3");
        // [0] sCoef -- color saturation
        // [1] vCoef -- shine brightness (intensity)
        // [2] sCoef1 -- shine saturation (color bleeding)
        // [3] bCoef -- darkness (more is less)
        Api.meshfxMsg("shaderVec4", 0, 2, "1.0 0.2 0.2 1.0");

        //Brows color
        Api.meshfxMsg("shaderVec4", 0, 4, "0.0 0.0 0.0 0.1");

        //Skin color
        Api.meshfxMsg("shaderVec4", 0, 5, "0.88 0.67 0.58 0.0");

        //Lips blur 
        Api.meshfxMsg("shaderVec4", 0, 6, "0.4 0.0 0.0 0.0");


        
        Api.showRecordButton();
    };

    this.restart = function() {
        Api.meshfxReset();
        self.init();
    };

    this.faceActions = [function(){ Api.meshfxMsg("shaderVec4", 0, 0, "1."); }];
    this.noFaceActions = [function(){ Api.meshfxMsg("shaderVec4", 0, 0, "0."); }];

    this.videoRecordStartActions = [];
    this.videoRecordFinishActions = [];
    this.videoRecordDiscardActions = [this.restart];
}

configure(new Effect());
