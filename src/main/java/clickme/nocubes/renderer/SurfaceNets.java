package clickme.nocubes.renderer;

import clickme.nocubes.NoCubes;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class SurfaceNets {
   public static int[] cube_edges = new int[24];
   public static int[] edge_table = new int[256];

   public static float getBlockDensity(int x, int y, int z, IBlockAccess cache) {
      float dens = 0.0F;

      for(int k = 0; k < 2; ++k) {
         for(int j = 0; j < 2; ++j) {
            for(int i = 0; i < 2; ++i) {
               Block block = cache.func_147439_a(x - i, y - j, z - k);
               if(NoCubes.isBlockNatural(block)) {
                  ++dens;
               } else {
                  --dens;
               }
            }
         }
      }

      return dens;
   }

   public static boolean renderChunk(int pass, int cx, int cy, int cz, IBlockAccess cache, RenderBlocks renderer) {
      if(!NoCubes.isNoCubesEnabled) {
         return false;
      } else if(pass != 0) {
         return false;
      } else {
         Tessellator tess = Tessellator.INSTANCE;
         int[] dims = new int[]{16, 16, 16};
         int[] c = new int[]{cx, cy, cz};
         int[] x = new int[3];
         int[] r = new int[]{1, dims[0] + 3, (dims[0] + 3) * (dims[1] + 3)};
         float[] grid = new float[8];
         float[][] buffer = new float[r[2] * 2][3];
         int bufno = 1;

         for(x[2] = 0; x[2] < dims[2] + 1; r[2] = -r[2]) {
            int m = 1 + (dims[0] + 3) * (1 + bufno * (dims[1] + 3));

            for(x[1] = 0; x[1] < dims[1] + 1; m += 2) {
               for(x[0] = 0; x[0] < dims[0] + 1; ++m) {
                  int mask = 0;
                  int g = 0;

                  int meta;
                  int br;
                  for(int block = 0; block < 2; ++block) {
                     for(meta = 0; meta < 2; ++meta) {
                        for(br = 0; br < 2; ++g) {
                           float icon = getBlockDensity(c[0] + x[0] + br, c[1] + x[1] + meta, c[2] + x[2] + block, cache);
                           grid[g] = icon;
                           mask |= icon > 0.0F?1 << g:0;
                           ++br;
                        }
                     }
                  }

                  if(mask != 0 && mask != 255) {
                     Block var47 = Blocks.AIR;
                     meta = 0;

                     int tu0;
                     int var49;
                     label216:
                     for(br = -1; br < 2; ++br) {
                        for(var49 = -1; var49 < 2; ++var49) {
                           for(tu0 = -1; tu0 < 2; ++tu0) {
                              Block i = cache.func_147439_a(c[0] + x[0] + tu0, c[1] + x[1] + br, c[2] + x[2] + var49);
                              if(NoCubes.isBlockNatural(i) && var47 != Blocks.SNOW_LAYER && var47 != Blocks.GRASS) {
                                 var47 = i;
                                 meta = cache.func_72805_g(c[0] + x[0] + tu0, c[1] + x[1] + br, c[2] + x[2] + var49);
                                 if(i == Blocks.SNOW_LAYER || i == Blocks.GRASS) {
                                    break label216;
                                 }
                              }
                           }
                        }
                     }

                     int[] var48 = new int[]{c[0] + x[0], c[1] + x[1] + 1, c[2] + x[2]};

                     label193:
                     for(var49 = -1; var49 < 2; ++var49) {
                        for(tu0 = -2; tu0 < 3; ++tu0) {
                           for(int var52 = -1; var52 < 2; ++var52) {
                              Block tu1 = cache.func_147439_a(c[0] + x[0] + var52, c[1] + x[1] + var49, c[2] + x[2] + tu0);
                              if(!tu1.isOpaqueCube()) {
                                 var48[0] = c[0] + x[0] + var52;
                                 var48[1] = c[1] + x[1] + var49;
                                 var48[2] = c[2] + x[2] + tu0;
                                 break label193;
                              }
                           }
                        }
                     }

                     IIcon var50 = renderer.func_147787_a(var47, 1, meta);
                     double var51 = (double)var50.getMinU();
                     double var53 = (double)var50.getMaxU();
                     double tv0 = (double)var50.getMinV();
                     double tv1 = (double)var50.getMaxV();
                     int edgemask = edge_table[mask];
                     int ecount = 0;
                     float[] v = new float[]{0.0F, 0.0F, 0.0F};
                     int s = 0;

                     label176:
                     while(true) {
                        int tx;
                        int ty;
                        int i1;
                        int iu;
                        int iv;
                        int du;
                        if(s >= 12) {
                           float var54 = 1.0F / (float)ecount;

                           for(tx = 0; tx < 3; ++tx) {
                              v[tx] = (float)(c[tx] + x[tx]) + var54 * v[tx];
                           }

                           tx = x[0] == 16?0:x[0];
                           ty = x[1] == 16?0:x[1];
                           int var55 = x[2] == 16?0:x[2];
                           long var56 = (long)(tx * 3129871) ^ (long)var55 * 116129781L ^ (long)ty;
                           var56 = var56 * var56 * 42317861L + var56 * 11L;
                           v[0] = (float)((double)v[0] - ((double)((float)(var56 >> 16 & 15L) / 15.0F) - 0.5D) * 0.2D);
                           v[1] = (float)((double)v[1] - ((double)((float)(var56 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D);
                           v[2] = (float)((double)v[2] - ((double)((float)(var56 >> 24 & 15L) / 15.0F) - 0.5D) * 0.2D);
                           buffer[m] = v;
                           i1 = 0;

                           while(true) {
                              if(i1 >= 3) {
                                 break label176;
                              }

                              if((edgemask & 1 << i1) != 0) {
                                 iu = (i1 + 1) % 3;
                                 iv = (i1 + 2) % 3;
                                 if(x[iu] != 0 && x[iv] != 0) {
                                    du = r[iu];
                                    int dv = r[iv];
                                    tess.func_78380_c(var47.func_149677_c(Minecraft.getMinecraft().theWorld, var48[0], var48[1], var48[2]));
                                    tess.func_78378_d(var47.func_149720_d(cache, c[0] + x[0], c[1] + x[1], c[2] + x[2]));
                                    float[] v0 = buffer[m];
                                    float[] v1 = buffer[m - du];
                                    float[] v2 = buffer[m - du - dv];
                                    float[] v3 = buffer[m - dv];
                                    if((mask & 1) != 0) {
                                       tess.func_78374_a((double)v0[0], (double)v0[1], (double)v0[2], var51, tv1);
                                       tess.func_78374_a((double)v1[0], (double)v1[1], (double)v1[2], var53, tv1);
                                       tess.func_78374_a((double)v2[0], (double)v2[1], (double)v2[2], var53, tv0);
                                       tess.func_78374_a((double)v3[0], (double)v3[1], (double)v3[2], var51, tv0);
                                    } else {
                                       tess.func_78374_a((double)v0[0], (double)v0[1], (double)v0[2], var51, tv1);
                                       tess.func_78374_a((double)v3[0], (double)v3[1], (double)v3[2], var53, tv1);
                                       tess.func_78374_a((double)v2[0], (double)v2[1], (double)v2[2], var53, tv0);
                                       tess.func_78374_a((double)v1[0], (double)v1[1], (double)v1[2], var51, tv0);
                                    }
                                 }
                              }

                              ++i1;
                           }
                        }

                        if((edgemask & 1 << s) != 0) {
                           ++ecount;
                           tx = cube_edges[s << 1];
                           ty = cube_edges[(s << 1) + 1];
                           float tz = grid[tx];
                           float i1 = grid[ty];
                           float t = tz - i1;
                           if(Math.abs(t) > 0.0F) {
                              t = tz / t;
                              i1 = 0;

                              for(iu = 1; i1 < 3; iu <<= 1) {
                                 iv = tx & iu;
                                 du = ty & iu;
                                 if(iv != du) {
                                    v[i1] += iv != 0?1.0F - t:t;
                                 } else {
                                    v[i1] += iv != 0?1.0F:0.0F;
                                 }

                                 ++i1;
                              }
                           }
                        }

                        ++s;
                     }
                  }

                  ++x[0];
               }

               ++x[1];
            }

            ++x[2];
            bufno ^= 1;
         }

         return true;
      }
   }

   static {
      int k = 0;

      int i;
      int em;
      int j;
      for(i = 0; i < 8; ++i) {
         for(em = 1; em <= 4; em <<= 1) {
            j = i ^ em;
            if(i <= j) {
               cube_edges[k++] = i;
               cube_edges[k++] = j;
            }
         }
      }

      for(i = 0; i < 256; ++i) {
         em = 0;

         for(j = 0; j < 24; j += 2) {
            boolean a = (i & 1 << cube_edges[j]) != 0;
            boolean b = (i & 1 << cube_edges[j + 1]) != 0;
            em |= a != b?1 << (j >> 1):0;
         }

         edge_table[i] = em;
      }

   }
}
