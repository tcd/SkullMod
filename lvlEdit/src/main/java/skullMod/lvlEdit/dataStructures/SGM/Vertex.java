package skullMod.lvlEdit.dataStructures.SGM;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class Vertex implements Serializable{
    public Position position;
    public Normals normals;
    public UV uv;
    public Color c; //uchar4 * 4 = RGBA (or BGRA, have to see what direct xPos prefers)

    //The following part is optional, it will exist if any joints are referenced.
    //Weights used for vertex skinning: http://tech-artists.org/wiki/Vertex_Skinning
    //More info: http://http.developer.nvidia.com/CgTutorial/cg_tutorial_chapter06.html
    //These are set to null if they are not needed, FIXME add fitting method to add them later on
    //The bone matrix array should be supplied programatically?
    //TODO GUESS: other joint stuff is supplied within attached sgs file
    public BlendIndices blendIndices;
    public BlendWeights blendWeights;

    //Misc attributes to determine if the blend indices and weights are relevant
    public boolean boneInfo;

    public Vertex(){
        this.position = new Position();
        this.normals = new Normals();
        this.uv = new UV();
        this.c = new Color();
    }

    public Vertex(DataInputStream dis, long attributeLengthPerVertex, String dataFormatString) throws IOException {
        if(attributeLengthPerVertex == 36){ boneInfo = false;}
        if(attributeLengthPerVertex == 44){ boneInfo = true; }

        //TODO check dataFormatString and general data checks


        position = new Position(dis);
        normals = new Normals(dis);
        uv = new UV(dis);
        c = new Color(dis);
        /**
        if(boneInfo){
            blendIndices = new BlendIndices(dis);
            blendWeights = new BlendWeights(dis);
        }else{
            blendIndices = null;
            blendWeights = null;
        }
        */

        //Data vertex attribute data for skinning is probably in sgs?
        //Just consume the 8 unknown bytes
        if(boneInfo){ dis.read(new byte[8]); }
    }

    public void writeToStream(DataOutputStream outputStream) throws IOException{
        //Write data according to: float p[3],n[3],uv[2]; uchar4 c;
        outputStream.writeFloat(position.xPos);
        outputStream.writeFloat(position.yPos);
        outputStream.writeFloat(position.zPos);

        outputStream.writeFloat(normals.normalX);
        outputStream.writeFloat(normals.normalY);
        outputStream.writeFloat(normals.normalZ);

        outputStream.writeFloat(uv.u);
        outputStream.writeFloat(uv.v);

        //FIXME is rgba correct? (could be abgr or something else)
        outputStream.writeByte(c.r);
        outputStream.writeByte(c.g);
        outputStream.writeByte(c.b);
        outputStream.writeByte(c.a);
        if(boneInfo){
            //TODO add bone info data
            //blendIndices.blendIndices;
            //FIXME might produce error
        }
    }
}
