package uk.ac.dundee.computing.aec.instagrim.models;

/*
 * Expects a cassandra columnfamily defined as
 * use keyspace2;
 CREATE TABLE Tweets (
 user varchar,
 interaction_time timeuuid,
 tweet varchar,
 PRIMARY KEY (user,interaction_time)
 ) WITH CLUSTERING ORDER BY (interaction_time DESC);
 * To manually generate a UUID use:
 * http://www.famkruithof.net/uuid/uuidgen
 */
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.Bytes;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.jhlabs.image.DissolveFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import static org.imgscalr.Scalr.*;
import org.imgscalr.Scalr.Method;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.jhlabs.image.*;
import uk.ac.dundee.computing.aec.instagrim.lib.*;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
//import uk.ac.dundee.computing.aec.stores.TweetStore;

public class PicModel {

    Cluster cluster;

    public void PicModel() {

    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public void setViewtimes(java.util.UUID picid) {
        Session session = cluster.connect("instagrim");

        String code = "select viewtimes from Pics where picid=?";
        PreparedStatement ps = session.prepare(code);
        BoundStatement boundStatement = new BoundStatement(ps);
        ResultSet rs = session.execute(boundStatement.bind(picid));
        String num = "\n";
        for (Row row : rs) {
            num = row.toString();
        }
        int times = Integer.parseInt(num.substring(4, num.length() - 1));
        System.out.println(times);
        times = times + 1;
        num = Integer.toString(times);

        code = "update Pics set viewtimes=" + num + " where picid=?";
        ps = session.prepare(code);
        boundStatement = new BoundStatement(ps);
        session.execute(boundStatement.bind(picid));
        session.close();
    }

    public void setLikes(java.util.UUID picid) {
        Session session = cluster.connect("instagrim");

        String code = "select likes from Pics where picid=?";
        PreparedStatement ps = session.prepare(code);
        BoundStatement boundStatement = new BoundStatement(ps);
        ResultSet rs = session.execute(boundStatement.bind(picid));
        String num = "\n";
        for (Row row : rs) {
            num = row.toString();
        }
        int times = Integer.parseInt(num.substring(4, num.length() - 1));
        System.out.println(times);
        times = times + 1;
        num = Integer.toString(times);

        code = "update Pics set likes=" + num + " where picid=?";
        ps = session.prepare(code);
        boundStatement = new BoundStatement(ps);
        session.execute(boundStatement.bind(picid));
        session.close();
    }
    
    public void setUnlikes(java.util.UUID picid) {
        Session session = cluster.connect("instagrim");

        String code = "select unlikes from Pics where picid=?";
        PreparedStatement ps = session.prepare(code);
        BoundStatement boundStatement = new BoundStatement(ps);
        ResultSet rs = session.execute(boundStatement.bind(picid));
        String num = "\n";
        for (Row row : rs) {
            num = row.toString();
        }
        int times = Integer.parseInt(num.substring(4, num.length() - 1));
        System.out.println(times);
        times = times + 1;
        num = Integer.toString(times);

        code = "update Pics set unlikes=" + num + " where picid=?";
        ps = session.prepare(code);
        boundStatement = new BoundStatement(ps);
        session.execute(boundStatement.bind(picid));
        session.close();
    }
    
    
    public void setComment(String text, java.util.UUID picid, String user) {
        System.out.println("in setComment");

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = df.format(new Date());
        System.out.println(time);

        text = "{\'" + time + "(" + user + ")" + ":" + text + "$\'}";
        Session session = cluster.connect("instagrim");
        String code = "update Pics set comment=comment+" + text + " where picid=?";
        System.out.println(code);
        PreparedStatement ps = session.prepare(code);
        System.out.println("......a");
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind(picid));
        session.close();
    }

    public String getComment(java.util.UUID picid) {
        Session session = cluster.connect("instagrim");
        String ret = "\n";
        ResultSet rs = null;
        PreparedStatement ps = null;
        ps = session.prepare("select comment from Pics where picid=?");
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute(
                boundStatement.bind(picid));
        if (rs.isExhausted()) {
            System.out.println("no comment");
            return null;
        } else {

            for (Row row : rs) {
                ret = row.toString();
            }
        }
        ret = ret.substring(5, ret.length() - 2);
        System.out.println(ret);
        ret = ret.replace("$,", "<br>");
        ret = ret.replace("$", "<br>");
        if (ret.equals("UL")) {
            return "Wait for you to add comment!";
        } else {
            ret = "Comment:<br>  " + ret;
            return ret;
        }
    }

    public String getInfo(java.util.UUID picid) {
        Session session = cluster.connect("instagrim");
        String ret = "\n";
        ResultSet rs = null;
        PreparedStatement ps = null;
        ps = session.prepare("select user,viewtimes,likes,unlikes from Pics where picid=?");
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute(
                boundStatement.bind(picid));
        if (rs.isExhausted()) {
            System.out.println("views");
            return null;
        } else {

            for (Row row : rs) {
                ret = row.toString();
            }
        }
        System.out.println(ret.substring(4, ret.length() - 1));
        return ret.substring(4, ret.length() - 1);
    }

    public void insertUserprofile(byte[] b, String type, String name, String user, int effecttype) {
        try {
            Convertors convertor = new Convertors();

            String types[] = Convertors.SplitFiletype(type);
            ByteBuffer buffer = ByteBuffer.wrap(b);
            int length = b.length;
            java.util.UUID picid = convertor.getTimeUUID();

            //The following is a quick and dirty way of doing this, will fill the disk quickly !
            Boolean success = (new File("/var/tmp/instagrim/")).mkdirs();
            FileOutputStream output = new FileOutputStream(new File("/var/tmp/instagrim/" + picid));

            output.write(b);
            byte[] thumbb = picresize(picid.toString(), types[1], effecttype);
            int thumblength = thumbb.length;
            ByteBuffer thumbbuf = ByteBuffer.wrap(thumbb);
            byte[] processedb = picdecolour(picid.toString(), types[1], effecttype);
            ByteBuffer processedbuf = ByteBuffer.wrap(processedb);
            int processedlength = processedb.length;
            Session session = cluster.connect("instagrim");

            PreparedStatement psInsertPic = session.prepare("insert into pics ( viewtimes, picid, image,thumb,processed, user, interaction_time,imagelength,thumblength,processedlength,type,name) values(?,?,?,?,?,?,?,?,?,?,?,?)");
            System.out.println("Error --> statement1");
            PreparedStatement psInsertPicToUser = session.prepare("update userprofiles set picid=? where login=?");
            System.out.println("Error --> statement2");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
            BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);

            Date DateAdded = new Date();
            session.execute(bsInsertPic.bind(-1, picid, buffer, thumbbuf, processedbuf, user, DateAdded, length, thumblength, processedlength, type, name));
            session.execute(bsInsertPicToUser.bind(picid, user));
            session.close();

        } catch (IOException ex) {
            System.out.println("Error --> " + ex);
        }
    }

    public void insertPic(byte[] b, String type, String name, String user, int effecttype) {
        try {
            Convertors convertor = new Convertors();

            String types[] = Convertors.SplitFiletype(type);
            ByteBuffer buffer = ByteBuffer.wrap(b);
            int length = b.length;
            java.util.UUID picid = convertor.getTimeUUID();

            //The following is a quick and dirty way of doing this, will fill the disk quickly !
            Boolean success = (new File("/var/tmp/instagrim/")).mkdirs();
            FileOutputStream output = new FileOutputStream(new File("/var/tmp/instagrim/" + picid));

            output.write(b);
            byte[] thumbb = picresize(picid.toString(), types[1], effecttype);
            int thumblength = thumbb.length;
            ByteBuffer thumbbuf = ByteBuffer.wrap(thumbb);

            byte[] processedb = picdecolour(picid.toString(), types[1], effecttype);
            ByteBuffer processedbuf = ByteBuffer.wrap(processedb);
            int processedlength = processedb.length;

            Session session = cluster.connect("instagrim");

            PreparedStatement psInsertPic = session.prepare("insert into pics ( viewtimes,likes,unlikes,picid, image,thumb,processed, user, interaction_time,imagelength,thumblength,processedlength,type,name) values(0,0,0,?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement psInsertPicToUser = session.prepare("insert into userpiclist ( picid, user, pic_added) values(?,?,?)");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
            BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);

            Date DateAdded = new Date();
            session.execute(bsInsertPic.bind(picid, buffer, thumbbuf, processedbuf, user, DateAdded, length, thumblength, processedlength, type, name));
            session.execute(bsInsertPicToUser.bind(picid, user, DateAdded));
            session.close();

        } catch (IOException ex) {
            System.out.println("Error --> " + ex);
        }
    }

    public byte[] picresize(String picid, String type, int effecttype) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage thumbnail = createThumbnail(BI, effecttype, type);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, type, baos);
            baos.flush();

            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }

    public byte[] picdecolour(String picid, String type, int effecttype) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage processed = createProcessed(BI, effecttype, type);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(processed, type, baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }

    public static BufferedImage createThumbnail(BufferedImage img, int effecttype, String type) throws IOException {
        int Width = img.getWidth() - 1;
        BufferedImage timg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        AbstractBufferedImageOp tmp;
        switch (effecttype) {
            case 1:
                timg = resize(img, Method.SPEED, Width, OP_ANTIALIAS, OP_GRAYSCALE);
                break;
            case 2:
                tmp = new DissolveFilter();
                tmp.filter(img, timg);
                break;
            case 3:
                tmp = new SolarizeFilter();
                tmp.filter(img, timg);
                break;
            case 4:
                tmp = new KaleidoscopeFilter();
                tmp.filter(img, timg);
                break;
            case 5:
                tmp = new DisplaceFilter();
                tmp.filter(img, timg);
                break;
            case 10:
                timg = img;
                break;
            // Let's add a little border before we return result.
        }
        timg = resize(timg, Method.SPEED, 400, OP_ANTIALIAS);
        return timg;
    }

    public static BufferedImage createProcessed(BufferedImage img, int effecttype, String type) throws IOException {
        int Width = img.getWidth() - 1;
        BufferedImage timg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        AbstractBufferedImageOp tmp;
        switch (effecttype) {
            case 1:
                timg = resize(img, Method.SPEED, Width, OP_ANTIALIAS, OP_GRAYSCALE);
                break;
            case 2:
                tmp = new DissolveFilter();
                tmp.filter(img, timg);
                break;
            case 3:
                tmp = new SolarizeFilter();
                tmp.filter(img, timg);
                break;
            case 4:
                tmp = new KaleidoscopeFilter();
                tmp.filter(img, timg);
                break;
            case 5:
                tmp = new DisplaceFilter();
                tmp.filter(img, timg);
                break;
            case 10:
                timg = img;
                break;
            // Let's add a little border before we return result.
        }
        return timg;
    }

    public java.util.LinkedList<Pic> getPicsForUser(String User) {
        java.util.LinkedList<Pic> Pics = new java.util.LinkedList<>();
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select picid from userpiclist where user =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        User));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return null;
        } else {
            for (Row row : rs) {
                Pic pic = new Pic();
                java.util.UUID UUID = row.getUUID("picid");
                System.out.println("UUID" + UUID.toString());
                pic.setUUID(UUID);
                Pics.add(pic);

            }
        }
        return Pics;
    }

    public java.util.LinkedList<Pic> getAllpics() {
        java.util.LinkedList<Pic> Pics = new java.util.LinkedList<>();
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select picid,viewtimes from pics");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute(
                boundStatement.bind());
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return null;
        } else {
            for (Row row : rs) {
                Pic pic = new Pic();
                java.util.UUID UUID = row.getUUID("picid");
                int num = row.getInt("viewtimes");
                if (num == -1) {
                    continue;
                }
                System.out.println("UUID" + UUID.toString());
                pic.setUUID(UUID);
                Pics.add(pic);
            }
        }
        return Pics;
    }

    public java.util.LinkedList<String> getCommentForPics(java.util.UUID picid) {
        java.util.LinkedList<String> comment = new java.util.LinkedList<>();
        Session session = cluster.connect("instagrim");

        PreparedStatement ps = session.prepare("select comment from Pics where picid =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        picid));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return null;
        } else {
            for (Row row : rs) {
                comment.add(rs.toString());
            }
        }
        return comment;
    }

    public Pic getPic(int image_type, java.util.UUID picid) {
        Session session = cluster.connect("instagrim");
        ByteBuffer bImage = null;
        String type = null;
        int length = 0;
        try {
            Convertors convertor = new Convertors();
            ResultSet rs = null;
            PreparedStatement ps = null;

            if (image_type == Convertors.DISPLAY_IMAGE) {

                ps = session.prepare("select image,imagelength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_THUMB) {
                ps = session.prepare("select thumb,imagelength,thumblength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                ps = session.prepare("select processed,processedlength,type from pics where picid =?");
            }
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            picid));

            if (rs.isExhausted()) {
                System.out.println("No Images returned");
                return null;
            } else {
                for (Row row : rs) {
                    if (image_type == Convertors.DISPLAY_IMAGE) {
                        bImage = row.getBytes("image");
                        length = row.getInt("imagelength");
                    } else if (image_type == Convertors.DISPLAY_THUMB) {
                        bImage = row.getBytes("thumb");
                        length = row.getInt("thumblength");

                    } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                        bImage = row.getBytes("processed");
                        length = row.getInt("processedlength");
                    }

                    type = row.getString("type");

                }
            }
        } catch (Exception et) {
            System.out.println("Can't get Pic" + et);
            return null;
        }
        session.close();
        Pic p = new Pic();
        p.setPic(bImage, length, type);

        return p;

    }

}
