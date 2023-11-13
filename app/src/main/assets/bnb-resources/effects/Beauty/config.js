let strength = 0;
function Effect() {
    var self = this;

    this.meshes = [
        { file: "face.bsm2", anims: [
                        { a: "Take 001", t: 4958 },
                ],

                is_physics_applied: Boolean("False".toLowerCase())
        },
    ];

    this.play = function() {
        var now = (new Date()).getTime();
        for(var i = 0; i < self.meshes.length; i++) {
                if(now > self.meshes[i].endTime) {
                        self.meshes[i].animIdx = (self.meshes[i].animIdx + 1)%self.meshes[i].anims.length;
                        if (!self.meshes[i].is_physics_applied) {
                                Api.meshfxMsg("animOnce", i, 0, self.meshes[i].anims[self.meshes[i].animIdx].a);
                        }
                        self.meshes[i].endTime = now + self.meshes[i].anims[self.meshes[i].animIdx].t;
                }
        }

        // if(Api.isMouthOpen()) {
        //  Api.hideHint();
        // }
    };

    this.init = function() {
        Api.meshfxMsg("spawn", 1, 0, "!glfx_FACE");

        Api.meshfxMsg("spawn", 0, 0, "face.bsm2");
        // Api.meshfxMsg("animOnce", 0, 0, "");
        // Api.meshfxMsg("animOnce", 0, 1, "");







        for(var i = 0; i < self.meshes.length; i++) {
            self.meshes[i].animIdx = -1;
            self.meshes[i].endTime = 0;
        }
        self.faceActions = [self.play];

        // Api.showHint("Open mouth");

        Api.showRecordButton();
    };

    this.restart = function() {
        Api.meshfxReset();


        self.init();
    };

    this.faceActions = [];
    this.noFaceActions = [];

    this.videoRecordStartActions = [];
    this.videoRecordFinishActions = [];
    this.videoRecordDiscardActions = [this.restart];
}

configure(new Effect());

function setMorphingStrength(str){
    bnb.scene.getComponents(bnb.ComponentType.FACE_MORPHING)[0].asFaceMorphing().setWeight(str);
}

function setAll(a = 0){
    strength = a <= 1.5 ? a : 1.5;
    setMorphingStrength(strength/1.5);
    Api.meshfxMsg("shaderVec4", 0, 0, strength/1.5 +" 0 0 0")
}

setAll();