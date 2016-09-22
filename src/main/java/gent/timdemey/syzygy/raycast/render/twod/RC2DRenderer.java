package gent.timdemey.syzygy.raycast.render.twod;

import gent.timdemey.syzygy.core.FrameInfo;
import gent.timdemey.syzygy.core.RenderInfo;
import gent.timdemey.syzygy.raycast.render.RCRenderer;
import gent.timdemey.syzygy.raycast.world.RCStateInfo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * @author Timmos
 */
public class RC2DRenderer implements RCRenderer {

    private final RC2DRenderInfo ri;

    public RC2DRenderer() {
        ri = new RC2DRenderInfo();
    }

    @Override
    public void renderAll(Graphics2D g, FrameInfo fInfo, RenderInfo rInfo, RCStateInfo sInfo) {
        renderBackground(newg(g), fInfo, rInfo, sInfo);
        renderGrid(newflipg(g, rInfo), fInfo, rInfo, sInfo);
        renderPlayer(newflipg(g, rInfo), fInfo, rInfo, sInfo);
        renderWallpoints(newflipg(g, rInfo), fInfo, rInfo, sInfo);
        renderText(newg(g), fInfo, rInfo, sInfo);
    }

    private static Graphics2D newg(Graphics g) {
        return (Graphics2D) g.create();
    }

    private static Graphics2D newflipg(Graphics g, RenderInfo rInfo) {
        Graphics2D gg = newg(g);
        gg.translate(0, rInfo.height);
        gg.scale(1, -1);
        return gg;
    }

    private static void renderBackground(Graphics2D g, FrameInfo fInfo, RenderInfo info, RCStateInfo sInfo) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, info.width, info.height);
    }

    private void renderGrid(Graphics2D g, FrameInfo fInfo, RenderInfo info, RCStateInfo sInfo) {
        ri.walls_x = sInfo.WALLS[0].length;
        ri.walls_y = sInfo.WALLS.length;

        ri.wallW = info.width / ri.walls_x;
        ri.wallH = info.height / ri.walls_y;

        ri.gridW = ri.wallW * ri.walls_x;
        ri.gridH = ri.wallH * ri.walls_y;

        g.setColor(Color.darkGray);
        for (int i = 1; i < ri.walls_x; i++) {
            int x = i * ri.wallW;
            g.drawLine(x, 0, x, ri.gridH);
        }
        for (int j = 1; j < ri.walls_y; j++) {
            int y = j * ri.wallH;
            g.drawLine(0, y, ri.gridW, y);
        }
    }

    private void renderPlayer(Graphics2D g, FrameInfo fInfo, RenderInfo rInfo, RCStateInfo sInfo) {
        g.setColor(Color.green);

        // user space
        double us_x = sInfo.T_trs[0][0];
        double us_y = sInfo.T_trs[1][0];

        // screen space, 1 unit => wall width/height
        int scr_x = (int) (us_x * ri.wallW);
        int scr_y = (int) (us_y * ri.wallH);

        g.fillOval(scr_x - 1, scr_y - 1, 3, 3);
    }

    private void renderWallpoints(Graphics2D g, FrameInfo fInfo, RenderInfo info, RCStateInfo sInfo) {
        g.setColor(Color.red);
        double[][] gridhits = sInfo.wall.gridhits;

        for (int k = 0; k < sInfo.wall.leaps; k++) {
            int scr_x = (int) (gridhits[k][0] * ri.wallW);
            int scr_y = (int) (gridhits[k][1] * ri.wallH);

            g.fillOval(scr_x - 1, scr_y - 1, 3, 3);
        }
    }

    private static void renderText(Graphics2D g, FrameInfo fInfo, RenderInfo info, RCStateInfo sInfo) {
        String p_posstr = String.format("POS=%.2f;%.2f", sInfo.T_trs[0][0], sInfo.T_trs[1][0]);
        String p_rotstr = String.format("ROT=%.2f", sInfo.rotangle);
        g.setColor(Color.YELLOW);
        g.setFont(Font.decode("Arial 9"));
        g.drawString(fInfo.currFPS + " FPS", 1, 10);
        g.drawString(p_posstr, 1, 30);
        g.drawString(p_rotstr, 1, 40);
        g.drawString(sInfo.wall.leaps + " grid hits", 1, 50);
        // g.drawString(sInfo.wall.gridhits.length + " grid hits", 10, 30);
    }
}